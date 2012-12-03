/**
 * NewProject.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for creating new projects.
 * New projects are associated with zero or more existing users.
 * When successfully finished, this servlet forwards the user
 * to the page for managing projects.</p>
 * <p>See <tt>newuser.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class NewProject extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(NewProject.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String NAME = "name";
    private final static String USERS = "users";
    private final static String ALL = "all";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/manageprojects.jsp";

    /**
     * URL triggered if the project name submitted is already in use.
     */
    private final static String URL_DUPLICATE_ERROR =
        "/jsp/admin/newproject.jsp?error=duplicate";

    /**
     * URL triggered when an empty project name is submitted.
     */
    private final static String URL_NONAME_ERROR =
        "/jsp/admin/newproject.jsp?error=noname";

    /**
     * URL triggered if the list of users associated with the new project
     * is empty.
     */
    private final static String URL_NOUSERS_ERROR =
        "/jsp/admin/newproject.jsp?projname={0}&error=nousers";

    private void createProject(QueryDB query, String name, String[] users)
        throws ServletException, PersistenceException
    {
        Project proj = new Project();
        proj.setName(name);
        proj.setCreationDate(new Date());
        proj.setState(Project.ACTIVE);
        try {
            query.getDb().create(proj);
        } catch (ClassNotPersistenceCapableException cnpce) {
            throw (ServletException)new ServletException
                ("Could not create new Project").initCause(cnpce);
        } catch (DuplicateIdentityException die) {
            throw (ServletException)new ServletException
                ("Could not create new Project").initCause(die);
        } catch (TransactionNotInProgressException tnipe) {
            throw (ServletException)new ServletException
                ("Could not create new Project").initCause(tnipe);
        }

        // Check first element in users[] array. If it is
        // ALL, then we associate all users with the new
        // project. Otherwise, we only associate those users
        // that are selected.
        ArrayList userList = new ArrayList();
        if (users[0].equals(ALL)) {
            try {
                Collection all = query.getAllFaqUsers(false);
                userList.addAll(all);
            } catch (UserNotFoundException pnfe) {
                throw (ServletException)new ServletException
                    ("Could not create new Project").initCause(pnfe);
            }
        } else {
            for (int k = 0; k < users.length; k++) {
                try {
                    FaqUser fuser = query.getFaqUser(users[k], false);
                    userList.add(fuser);
                } catch (UserNotFoundException pnfe) {
                    throw (ServletException)new ServletException
                        ("Could not create new Project").initCause(pnfe);
                }
            }
        }

        // For each user, add the project.
        Iterator it = userList.iterator();
        while (it.hasNext()) {
            FaqUser fuser = (FaqUser)it.next();
            fuser.addProject(proj);
        }
    }

    /**
     * Do not use this method.  New projects should be created via POST.
     *
     * @throws ServletException always, because GET is not used to
     * create new projects.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        ServletException srvEx = new ServletException
            ("Cannot create a new project with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * Create a new project.
     *
     * @param req contains the POST data identifying the new project.
     * @param resp used to output HTTP response.
     * @throws ServletException if anything goes wrong, including
     * problems with the backend.
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // Get handle on database.
        QueryDB query = null;
        try {
            query = new QueryDB(getServletContext());
        } catch (DatabaseNotFoundException dnfe) {
            serveError(req, resp, dnfe);
            return;
        }

        String name = getTrimmedParameter(req, NAME);
        if (null == name || name.length() <= 0) {
            forwardRequest(query.getDb(), URL_NONAME_ERROR, req, resp);
            return;
        }

        String[] users = req.getParameterValues(USERS);
        if (null == users || users.length <= 0) {
            Object[] urlArgs = {name};
            String path = MessageFormat.format(URL_NOUSERS_ERROR, urlArgs);
            forwardRequest(query.getDb(), path, req, resp);
            return;
        }

        Exception createEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Create record in back-end.
            createProject(query, name, users);

            // End transaction.
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            createEx = ex;
            try {
                // Rollback the transaction.
                query.getDb().rollback();
            } catch (TransactionNotInProgressException tnpe) {
                // We do not care.
                logger.info("could not rollback transaction: " + tnpe);
            }
        } finally {
            // Close the database below.  See comment in Register.java.
        }

        if (null != createEx) {
            if (createEx instanceof PersistenceException) {
                forwardRequest(query.getDb(), URL_DUPLICATE_ERROR, req, resp);
            } else {
                try {
                    query.getDb().close();
                } catch (PersistenceException pe) {
                    // See comment in Register.java.
                }
                serveError(req, resp, createEx);
            }
        } else {
            forwardRequest(query.getDb(), FORWARD_URL, req, resp);
        }
    }
}
