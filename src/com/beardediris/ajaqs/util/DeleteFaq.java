/**
 * DeleteFaq.java
 */

package com.beardediris.ajaqs.util;

import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This servlet is used to delete FAQs. It should only
 * be invoked by end-users with administrative access to
 * Ajaqs.  We do not enforce that restriction here, but instead
 * rely on the security constraints in the deployment
 * descriptor, <tt>web.xml</tt>, for setting up the access
 * policy for this servlet.</p>
 */
public class DeleteFaq extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(DeleteFaq.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/editproject.jsp?project={0}";

    /**
     * Delete the Faq.
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

        int projId, faqId;
        try {
            projId = parseInteger(req, PROJECT);
            faqId = parseInteger(req, FAQ);
        } catch (Exception ex) {
            serveError(req, resp, ex);
            return;
        }

        Exception deleteEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            Project theProj = query.getProject(projId);
            Faq theFaq = theProj.removeFaq(faqId);
            query.getDb().remove(theFaq);

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
            Object[] ids = {new Integer(projId)};
            String path = MessageFormat.format(FORWARD_URL, ids);
            forwardRequest(query.getDb(), path, req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
