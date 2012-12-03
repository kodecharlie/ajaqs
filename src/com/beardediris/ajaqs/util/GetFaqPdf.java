/**
 * GetFaqPdf.java
 */

package com.beardediris.ajaqs.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This class implements a servlet used to download an FAQ
 * as a PDF document.  See <tt>web.xml</tt> and
 * <tt>projectpage.jsp</tt> for how this servlet is used.</p>
 */
public final class GetFaqPdf extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(GetFaqPdf.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";

    /**
     * Bogus faq ID used to denote absence of FAQ parameter.
     * See notes below.
     */
    private final static int NEED_FULL_PROJ = -1000;

    private void insertFaq(Faq faq, Document doc, Font titFont)
        throws DocumentException
    {
        // Make font for highlighting questions.
        Font qfont = FontFactory.getFont
            (FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 255));

        // Add title for FAQ.
        doc.add(new Paragraph(faq.getName(), titFont));

        // Each question will be a section, and each answer to that
        // question will be a paragraph in that section.
        Iterator questIt = faq.getQuestions().iterator();
        while (questIt.hasNext()) {
            Question q = (Question)questIt.next();

            logger.info("Adding question");
            Paragraph pq = new Paragraph(q.getQuestion(), qfont);
            doc.add(pq);

            // Add list of enumerated answers for question.
            List ansList = new List(true, 20);
            Iterator ansIt = q.getAnswers().iterator();
            while (ansIt.hasNext()) {
                logger.info("Adding answer to list");
                Answer a = (Answer)ansIt.next();
                ListItem lit = new ListItem(a.getAnswer());
                ansList.add(lit);
            }

            logger.info("Adding list");
            doc.add(ansList);
            doc.add(Chunk.NEWLINE);
        }
    }

    private void servePdf(QueryDB query, String logon, int projId, int faqId,
                          HttpServletResponse resp)
        throws ServletException, IOException
    {
        // Get user.
        FaqUser fuser = null;
        try {
            fuser = query.getFaqUser(logon, false);
        } catch (UserNotFoundException unfe) {
            throw (ServletException)new ServletException
                ("FaqUser with logon \"" + logon + "\" not found")
                .initCause(unfe);
        }

        // Get project.
        Project proj = null;
        try {
            proj = fuser.getProject(projId);
        } catch (ProjectNotFoundException pnfe) {
            throw (ServletException)new ServletException
                ("Could not edit FAQ").initCause(pnfe);
        }

        // Get list of FAQs: this will be a single FAQ if the
        // faqId is specified; otherwise, all FAQs in the project
        // are contained in the list.
        ArrayList faqList = new ArrayList();
        if (faqId >= 0) {
            try {
                faqList.add(proj.getFaq(faqId));
            } catch (FaqNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Could not edit FAQ").initCause(fnfe);
            }
        } else {
            faqList.addAll(proj.getFaqs());
        }

        // Set the response header.
        resp.setContentType("application/pdf");
        resp.setHeader("Expires", "0");
        resp.setHeader("Cache-Control",
                       "must-revalidate, post-check=0, pre-check=0");
        resp.setHeader("Pragma", "public");

        // Title for the PDF document.
        StringBuffer title = new StringBuffer();
        title.append(proj.getName());

        // Timestamp of PDF document creation.
        String ts = (new Date()).toString();

        // FIXME: make page size configurable.
        OutputStream ostream = resp.getOutputStream();
        try {
            Document doc = new Document(PageSize.LETTER);
            PdfWriter.getInstance(doc, ostream);
            logger.info("Opening Document");
            doc.open();

            // Make font for title.
            Font titFont = FontFactory.getFont
                (FontFactory.HELVETICA, 14, Font.BOLD);

            // Make font for subtitle.
            Font subtitFont = FontFactory.getFont
                (FontFactory.HELVETICA, 10);

            logger.info("Adding title ... ");
            doc.add(new Paragraph(title.toString(), titFont));
            doc.add(new Paragraph(ts.toString(), subtitFont));
            doc.add(Chunk.NEWLINE);

            Iterator faqIt = faqList.iterator();
            while (faqIt.hasNext()) {
                Faq faq = (Faq)faqIt.next();
                insertFaq(faq, doc, titFont);
            }
            logger.info("closing Document");
            doc.close();
        } catch (DocumentException dex) {
            throw (ServletException)new ServletException
                ("Could not dynamically generate PDF output for FAQ")
                .initCause(dex);
        }
    }

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

        String logon = req.getRemoteUser();
        if (null == logon || logon.length() <= 0) {
            ServletException ex = new ServletException
                ("Remote user not authenticated");
            serveError(req, resp, ex);
            return;
        }

        int projId;
        try {
            projId = parseInteger(req, PROJECT);
        } catch (Exception ex) {
            serveError(req, resp, ex);
            return;
        }

        int faqId;
        try {
            faqId = parseInteger(req, FAQ);
        } catch (Exception ex) {
            // This is not strictly true:  For convenience,
            // we assume that if an exception occurs, then
            // the FAQ parameter is absent (ie, we are serving
            // up a PDF document containing the entire project).
            // Denote this case by setting faqId to NEED_FULL_PROJ.
            faqId = NEED_FULL_PROJ;
        }

        Exception pdfEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Create record in back-end.
            servePdf(query, logon, projId, faqId, resp);

            // End transaction.
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            pdfEx = ex;
            try {
                // Rollback the transaction.
                query.getDb().rollback();
            } catch (TransactionNotInProgressException tnpe) {
                // We do not care.
                logger.info("could not rollback transaction: " + tnpe);
            }
        } finally {
            try {
                query.getDb().close();
            } catch (PersistenceException pe) {
                // Ignore this exception because either we already have
                // served up the PDF, or pdfEx is non-null, which case we
                // deal with below.
            }
        }

        if (null != pdfEx) {
            serveError(req, resp, pdfEx);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
