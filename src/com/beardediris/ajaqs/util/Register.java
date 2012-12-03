/**
 * Register.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Role;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.RoleNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for creating new users.
 * New users are assigned the default user-role and associated
 * with zero or more projects.  When successfully finished, this
 * servlet forwards the user to the logon page.</p>
 * <p>See <tt>register.jsp</tt> for how this servlet is
 * invoked.</p>
 *
 * @see Submitter
 */
public final class Register extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(Register.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String LOGON = "logon";
    private final static String PASSWORD = "password";
    private final static String RETYPE = "retype";
    private final static String EMAIL = "email";
    private final static String PROJECTS = "projects";
    private final static String ALL = "all";

    /**
     * This is the name of a role defined in web.xml.
     * If you change this constant, then you must change that
     * file also.
     */
    private final static String USER_ROLE = "ajaqs-user";

    /**
     * Upon successful creation of a new user, we forward the
     * end-user back to the login page.  For Tomcat, this is
     * ok, as it remembers what original JSP under our security
     * regime was first requested before this registration; we
     * are properly forwarded to that page after the login completes.
     * For other web or app servers, I am not sure what happens.
     */
    private final static String FORWARD_URL =
        "/jsp/security/logon.jsp";

    /**
     * URL triggered when mismatched passwords are submitted.
     */
    private final static String URL_EMPTYLOGON_ERROR =
        "/jsp/security/register.jsp?error=emptylogon";

    /**
     * URL triggered when mismatched passwords are submitted.
     */
    private final static String URL_PASSWORDS_ERROR =
        "/jsp/security/register.jsp?error=passwords";

    /**
     * URL triggered when a duplicate username is submitted.
     */
    private final static String URL_DUPLICATE_ERROR =
        "/jsp/security/register.jsp?error=duplicate";

    private void createFaqUser(QueryDB query, FaqUser fuser,
                               String[] projects)
        throws ServletException, DuplicateIdentityException,
               PersistenceException
    {
        try {
            query.getDb().create(fuser);
        } catch (ClassNotPersistenceCapableException cnpce) {
            throw (ServletException)new ServletException
                ("Could not persist new FaqUser").initCause(cnpce);
        } catch (TransactionNotInProgressException tnipe) {
            throw (ServletException)new ServletException
                ("Bad transaction for new FaqUser").initCause(tnipe);
        }

        // Assign the new user the default user-role.
        ArrayList roles = new ArrayList();
        try {
            Role defaultRole = query.getRole(USER_ROLE);
            roles.add(defaultRole);
        } catch (RoleNotFoundException rnfe) {
            throw (ServletException)new ServletException
                ("Could not add roles to new FaqUser").initCause(rnfe);
        }
        fuser.setRoles(roles);

        // Check first element in projects[] array. If it is
        // ALL, then we associate all projects with the new
        // user. Otherwise, we only associate those projects
        // that are selected.
        ArrayList projList = new ArrayList();
        if (projects[0].equals(ALL)) {
            try {
                Collection all = query.getAllProjects();
                projList.addAll(all);
            } catch (ProjectNotFoundException pnfe) {
                throw (ServletException)new ServletException
                    ("Could not add projects to new FaqUser")
                    .initCause(pnfe);
            }
        } else {
            for (int k = 0; k < projects.length; k++) {
                int projId = Integer.parseInt(projects[k]);
                try {
                    Project p = query.getProject(projId);
                    projList.add(p);
                } catch (ProjectNotFoundException pnfe) {
                    throw (ServletException)new ServletException
                        ("Could not add project to new FaqUser")
                        .initCause(pnfe);
                }
            }
        }
        fuser.setProjects(projList);
    }

    /**
     * Do not use this method.  New users should be created via POST.
     *
     * @throws ServletException always, because GET is not used to
     * create new users.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        ServletException srvEx = new ServletException
            ("Cannot create a new user with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * Create a new user as per input parameters submitted via POST.
     *
     * @param req contains the POST data identifying the new user.
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

        String logon = getTrimmedParameter(req, LOGON);
        if (null == logon || logon.length() <= 0) {
            forwardRequest(query.getDb(), URL_EMPTYLOGON_ERROR, req, resp);
            return;
        }

        String password = getTrimmedParameter(req, PASSWORD);
        if (null == password || password.length() <= 0) {
            ServletException srvEx = new ServletException
                ("\"" + PASSWORD + "\" parameter must be specified");
            serveError(req, resp, srvEx);
            return;
        }

        String retype = getTrimmedParameter(req, RETYPE);
        if (null == retype || retype.length() <= 0) {
            ServletException srvEx = new ServletException
                ("\"" + RETYPE + "\" parameter must be specified");
            serveError(req, resp, srvEx);
            return;
        }

        // Email might be null.
        String email = getTrimmedParameter(req, EMAIL);

        String[] projects = req.getParameterValues(PROJECTS);
        if (null == projects || projects.length <= 0) {
            ServletException srvEx = new ServletException
                ("\"" + PROJECTS + "\" parameter must be specified");
            serveError(req, resp, srvEx);
            return;
        }

        // Make certain passwords match.
        if (! password.equals(retype)) {
            forwardRequest(query.getDb(), URL_PASSWORDS_ERROR, req, resp);
            return;
        }

        Date now = new Date();
        FaqUser fuser = new FaqUser();
        fuser.setLogon(logon);
        fuser.setPassword(password);
        fuser.setEmail(email);
        fuser.setCreationDate(now);
        fuser.setLastLoginDate(now);
        fuser.setState(FaqUser.ACTIVE);

        Exception createEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Create record in back-end.
            createFaqUser(query, fuser, projects);

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
            // Note: we do not close the database connection here.
            // Our convention is that the database connection will
            // be closed below either in Submitter.forwardRequest
            // or explicitly in case there is an exception that
            // we do not recognize; in the latter case, we serve
            // up an error page via Submitter.serveError.
        }

        if (null != createEx) {
            // Note: handle DuplicateIdentityException and
            // PersistenceException in the same way, because we have
            // observed that either can be triggered by a duplicate-key.
            if (createEx instanceof DuplicateIdentityException
                || createEx instanceof PersistenceException) {
                forwardRequest(query.getDb(), URL_DUPLICATE_ERROR, req, resp);
            } else {
                try {
                    query.getDb().close();
                } catch (PersistenceException pe) {
                    // We do not process this exception because we
                    // already are dealing with the exception createEx.
                    // FIXME? maybe we should combine both exceptions
                    // into a new one so that no information is lost.
                }
                serveError(req, resp, createEx);
            }
        } else {
            forwardRequest(query.getDb(), FORWARD_URL, req, resp);
        }
    }
}
