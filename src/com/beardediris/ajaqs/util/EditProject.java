/**
 * EditProject.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for editing projects.
 * When successfully finished, this servlet forwards the user
 * to the page for managing projects.</p>
 * <p>See <tt>editproject.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class EditProject extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(EditProject.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String NAME = "name";
    private final static String STATE = "state";
    private final static String USERS = "users";

    /**
     * Possible value for selections.
     */
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
        "/jsp/admin/editproject.jsp?project={0}&error=duplicate";

    /**
     * URL triggered when an empty project name is submitted.
     */
    private final static String URL_NONAME_ERROR =
        "/jsp/admin/editproject.jsp?project={0}&error=noname";

    /**
     * URL triggered if the list of users associated with the new project
     * is empty.
     */
    private final static String URL_NOUSERS_ERROR =
        "/jsp/admin/editproject.jsp?project={0}&error=nousers";

    private void editProject(QueryDB query, int projId, String name,
                             int state, String[] users)
        throws ServletException
    {
        // Get project.
        Project theProj = null;
        try {
            theProj = query.getProject(projId);
        } catch (ProjectNotFoundException pnfe) {
            throw (ServletException)new ServletException
                ("Could not edit project").initCause(pnfe);
        }

        // Get users.
        boolean addAllFlag = false;
        HashSet projUsers = new HashSet();
        if (users[0].equals(ALL)) {
            addAllFlag = true;
        } else {
            for (int k = 0; k < users.length; k++) {
                projUsers.add(users[k]);
            }
        }

        // Reset various properties for the project. Notice that
        // we blindly overwrite whatever values existed before.
        theProj.setName(name);
        theProj.setState(state);

        Collection userList = null;
        try {
            userList = query.getAllFaqUsers(false);
        } catch (UserNotFoundException unfe) {
            throw (ServletException)new ServletException
                ("Could not edit project").initCause(unfe);
        }

        // Cycle through the list of users.  If a user is found
        // to be in the projUsers list or the project is associated
        // with all users, then we add the project to the user;
        // otherwise, we remove the project from the user.
        Iterator it = userList.iterator();
        while (it.hasNext()) {
            FaqUser fu = (FaqUser)it.next();
            if (addAllFlag || projUsers.contains(fu.getLogon())) {
                fu.addProject(theProj);
            } else {
                fu.removeProject(theProj);
            }
        }
    }

    /**
     * Call the handler for POST.
     *
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    /**
     * Edit the project.
     *
     * @param req contains the POST data used to modify the project.
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

        int projId, state;
        try {
            projId = parseInteger(req, PROJECT);
            state = parseInteger(req, STATE);
        } catch (Exception ex) {
            serveError(req, resp, ex);
            return;
        }

        String name = getTrimmedParameter(req, NAME);
        if (null == name || name.length() <= 0) {
            Object[] ids = {new Integer(projId)};
            String path = MessageFormat.format(URL_NONAME_ERROR, ids);
            forwardRequest(query.getDb(), path, req, resp);
            return;
        }

        String[] users = req.getParameterValues(USERS);
        if (null == users || users.length <= 0) {
            Object[] ids = {new Integer(projId)};
            String path = MessageFormat.format(URL_NOUSERS_ERROR, ids);
            forwardRequest(query.getDb(), path, req, resp);
            return;
        }

        Exception editEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Edit record in back-end.
            editProject(query, projId, name, state, users);

            // End transaction.
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            editEx = ex;
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

        if (null != editEx) {
            if (editEx instanceof PersistenceException) {
                Object[] ids = {new Integer(projId)};
                String path = MessageFormat.format(URL_DUPLICATE_ERROR, ids);
                forwardRequest(query.getDb(), path, req, resp);
            } else {
                try {
                    query.getDb().close();
                } catch (PersistenceException pe) {
                    // See comment in Register.java.
                }
                serveError(req, resp, editEx);
            }
        } else {
            forwardRequest(query.getDb(), FORWARD_URL, req, resp);
        }
    }
}
