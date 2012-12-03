/**
 * EditUser.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Role;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.RoleNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for editing users.
 * When successfully finished, this servlet forwards the user
 * to the page for managing users.</p>
 * <p>See <tt>edituser.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class EditUser extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(EditUser.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String LOGON = "logon";
    private final static String EMAIL = "email";
    private final static String STATE = "state";
    private final static String ROLES = "roles";
    private final static String PROJECTS = "projects";

    /**
     * Possible value for selections.
     */
    private final static String ALL = "all";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/manageusers.jsp";

    private void editUser(QueryDB query, HttpServletRequest req)
        throws ServletException
    {
        String logon = getTrimmedParameter(req, LOGON);
        if (null == logon || logon.length() <= 0) {
            throw new ServletException
                ("\"" + LOGON + "\" parameter must be specified");
        }

        // Email might be null.
        String email = getTrimmedParameter(req, EMAIL);
        int state = parseInteger(req, STATE);

        String[] roles = req.getParameterValues(ROLES);
        if (null == roles || roles.length <= 0) {
            throw new ServletException
                ("\"" + ROLES + "\" parameter must be specified");
        }

        String[] projects = req.getParameterValues(PROJECTS);
        if (null == projects || projects.length <= 0) {
            throw new ServletException
                ("\"" + PROJECTS + "\" parameter must be specified");
        }

        // Get user.
        FaqUser fuser = null;
        try {
            fuser = query.getFaqUser(logon, false);
        } catch (UserNotFoundException unfe) {
            throw (ServletException)new ServletException
                ("Could not edit user").initCause(unfe);
        }

        // Get roles.
        ArrayList roleList = new ArrayList();
        if (roles[0].equals(ALL)) {
            try {
                Collection all = query.getAllRoles();
                roleList.addAll(all);
            } catch (RoleNotFoundException rnfe) {
                throw (ServletException)new ServletException
                    ("Could not edit user").initCause(rnfe);
            }
        } else {
            for (int k = 0; k < roles.length; k++) {
                try {
                    Role r = query.getRole(roles[k]);
                    roleList.add(r);
                } catch (RoleNotFoundException rnfe) {
                    throw (ServletException)new ServletException
                        ("Could not edit user").initCause(rnfe);
                }
            }
        }

        // Get projects.
        ArrayList projList = new ArrayList();
        if (projects[0].equals(ALL)) {
            try {
                Collection all = query.getAllProjects();
                projList.addAll(all);
            } catch (ProjectNotFoundException rnfe) {
                throw (ServletException)new ServletException
                    ("Could not edit user").initCause(rnfe);
            }
        } else {
            for (int k = 0; k < projects.length; k++) {
                int projId = -1;
                try {
                    projId = Integer.parseInt(projects[k]);
                } catch (NumberFormatException nfe) {
                    throw (ServletException)new ServletException
                        ("\"" + PROJECTS + "\" parameter must be a list "
                         + "of valid integers").initCause(nfe);
                }
                try {
                    Project p = query.getProject(projId);
                    projList.add(p);
                } catch (ProjectNotFoundException pnfe) {
                    throw (ServletException)new ServletException
                        ("Could not edit user").initCause(pnfe);
                }
            }
        }

        // Reset various properties for the user. Notice that
        // we blindly overwrite whatever values existed before.
        fuser.setEmail(email);
        fuser.setState(state);
        fuser.setRoles(roleList);
        fuser.setProjects(projList);
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
     * Edit the user.
     *
     * @param req contains the POST data used to modify the user.
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

        Exception createEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Edit record in back-end.
            editUser(query, req);

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
            try {
                query.getDb().close();
            } catch (PersistenceException pe) {
                // See comment in Register.java.
            }
            serveError(req, resp, createEx);
        } else {
            forwardRequest(query.getDb(), FORWARD_URL, req, resp);
        }
    }
}
