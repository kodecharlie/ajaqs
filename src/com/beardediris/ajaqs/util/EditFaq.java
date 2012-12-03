/**
 * EditFaq.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for editing FAQs.
 * When successfully finished, this servlet forwards the user
 * to the page for editing the current project.</p>
 * <p>See <tt>editfaq.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class EditFaq extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(EditFaq.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";
    private final static String NAME = "name";
    private final static String STATE = "state";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/editproject.jsp?project={0}";

    /**
     * URL triggered when an empty FAQ name is submitted.
     */
    private final static String URL_NONAME_ERROR =
        "/jsp/admin/editfaq.jsp?project={0}&faq={1}&error=noname";

    /**
     * URL triggered if the FAQ name submitted is already in use.
     */
    private final static String URL_DUPLICATE_ERROR =
        "/jsp/admin/editfaq.jsp?project={0}&faq={1}&error=duplicate";

    private void editFaq(QueryDB query, int projId, int faqId,
                         String name, int state)
        throws ServletException
    {
        // Get project.
        Project theProj = null;
        try {
            theProj = query.getProject(projId);
        } catch (ProjectNotFoundException pnfe) {
            throw (ServletException)new ServletException
                ("Could not edit FAQ").initCause(pnfe);
        }

        // Get faq.
        Faq theFaq = null;
        try {
            theFaq = theProj.getFaq(faqId);
        } catch (FaqNotFoundException fnfe) {
            throw (ServletException)new ServletException
                ("Could not edit FAQ").initCause(fnfe);
        }

        // Reset various properties for the FAQ. Notice that
        // we blindly overwrite whatever values existed before.
        theFaq.setName(name);
        theFaq.setState(state);
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
     * Edit the FAQ.
     *
     * @param req contains the POST data used to modify the FAQ.
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

        int projId, faqId, state;
        try {
            projId = parseInteger(req, PROJECT);
            faqId = parseInteger(req, FAQ);
            state = parseInteger(req, STATE);
        } catch (Exception ex) {
            serveError(req, resp, ex);
            return;
        }

        String name = getTrimmedParameter(req, NAME);
        if (null == name || name.length() <= 0) {
            Object[] ids = {new Integer(projId), new Integer(faqId)};
            String path = MessageFormat.format(URL_NONAME_ERROR, ids);
            forwardRequest(query.getDb(), path, req, resp);
            return;
        }

        Exception editEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Edit record in back-end.
            editFaq(query, projId, faqId, name, state);

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
                Object[] ids = {new Integer(projId), new Integer(faqId)};
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
            Object[] ids = {new Integer(projId)};
            String path = MessageFormat.format(FORWARD_URL, ids);
            forwardRequest(query.getDb(), path, req, resp);
        }
    }
}
