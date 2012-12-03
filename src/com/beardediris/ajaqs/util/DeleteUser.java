/**
 * DeleteUser.java
 */

package com.beardediris.ajaqs.util;

import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This servlet is used to delete users. It should only
 * be invoked by end-users with administrative access to
 * Ajaqs.  We do not enforce that restriction here, but instead
 * rely on the security constraints in the deployment
 * descriptor, <tt>web.xml</tt>, for setting up the access
 * policy for this servlet.</p>
 */
public class DeleteUser extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(DeleteUser.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String LOGON = "logon";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/manageusers.jsp";

    /**
     * Delete the user, and then forward the current page
     * to a suitable url.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
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
            ServletException srvEx = new ServletException
                ("\"" + LOGON + "\" parameter must be specified");
            serveError(req, resp, srvEx);
            return;
        }

        Exception deleteEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            FaqUser fuser = query.getFaqUser(logon, false);
            query.getDb().remove(fuser);

            // End transaction.
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            deleteEx = ex;
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

        if (null != deleteEx) {
            try {
                query.getDb().close();
            } catch (PersistenceException pe) {
                // See comment in Register.java.
            }
            serveError(req, resp, deleteEx);
        } else {
            forwardRequest(query.getDb(), FORWARD_URL, req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
