/**
 * NewFaq.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for creating a new FAQ for
 * an existing project.  When successfully finished, this
 * servlet forwards the user to the <tt>editproject.jsp</tt>
 * page for the project we just modified.</p>
 * <p>See <tt>newfaq.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class NewFaq extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(NewFaq.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String NAME = "name";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/editproject.jsp?project={0}";

    /**
     * URL triggered when an empty FAQ name is submitted.
     */
    private final static String URL_NONAME_ERROR =
        "/jsp/admin/newfaq.jsp?project={0}&error=noname";

    /**
     * URL triggered if the FAQ name submitted is already in use.
     */
    private final static String URL_DUPLICATE_ERROR =
        "/jsp/admin/newfaq.jsp?project={0}&error=duplicate";

    private void createFaq(QueryDB query, int projId, String faqName)
        throws ServletException, PersistenceException
    {
        Project theProj = null;
        try {
            theProj = query.getProject(projId);
        } catch (ProjectNotFoundException pnfe) {
            throw (ServletException)new ServletException
                ("Could not create new Faq").initCause(pnfe);
        }

        Faq faq = new Faq();
        faq.setName(faqName);
        faq.setCreationDate(new Date());
        faq.setState(Faq.ACTIVE);
        faq.setProject(theProj);
        try {
            query.getDb().create(faq);
        } catch (ClassNotPersistenceCapableException cnpce) {
            throw (ServletException)new ServletException
                ("Could not create new Faq").initCause(cnpce);
        } catch (DuplicateIdentityException die) {
            throw (ServletException)new ServletException
                ("Could not create new Faq").initCause(die);
        } catch (TransactionNotInProgressException tnipe) {
            throw (ServletException)new ServletException
                ("Could not create new Faq").initCause(tnipe);
        }
        theProj.addFaq(faq);
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
            ("Cannot create a new FAQ with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * Create a new FAQ.
     *
     * @param req contains the POST data identifying the new FAQ.
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

        int projId;
        try {
            projId = parseInteger(req, PROJECT);
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

        Exception createEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Create record in back-end.
            createFaq(query, projId, name);

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

        Object[] ids = {new Integer(projId)};
        if (null != createEx) {
            if (createEx instanceof PersistenceException) {
                String path = MessageFormat.format(URL_DUPLICATE_ERROR, ids);
                forwardRequest(query.getDb(), path, req, resp);
            } else {
                try {
                    query.getDb().close();
                } catch (PersistenceException pe) {
                    // See comment in Register.java.
                }
                serveError(req, resp, createEx);
            }
        } else {
            String path = MessageFormat.format(FORWARD_URL, ids);
            forwardRequest(query.getDb(), path, req, resp);
        }
    }
}
