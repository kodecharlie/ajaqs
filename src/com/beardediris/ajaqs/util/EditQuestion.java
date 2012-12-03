/**
 * EditQuestion.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.EmptyQuestionException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import com.beardediris.ajaqs.session.Constants;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is responsible for editing Questions.
 * When successfully finished, this servlet forwards the user
 * to the page for editing the current FAQ.</p>
 * <p>See <tt>editquestion.jsp</tt> for how this servlet is
 * invoked.</p>
 */
public final class EditQuestion extends Submitter
    implements Constants
{
    private static final Logger logger =
        Logger.getLogger(EditQuestion.class.getName());

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";
    private final static String QUESTION = "question";
    private final static String QSTRING = "qstring";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/admin/editfaq.jsp?project={0}&faq={1}";

    /**
     * This URL is triggered when the question submitted is empty.
     */
    private final static String URL_NOQUESTION_ERROR =
        "/jsp/admin/editquestion.jsp?project={0}&faq={1}&question={2}"
        + "&error=emptyquestion";

    /**
     * Transient fields.
     */
    private int m_projectId;
    private int m_faqId;
    private int m_questionId;

    private void editQuestion(QueryDB query, HashMap partList)
        throws ServletException, EmptyQuestionException
    {
        // Get project.
        Project project = null;
        Part part = (Part)partList.get(PROJECT);
        if (null != part && part.isParam()) {
            int projId = -1;
            try {
                projId = Integer.parseInt(((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + PROJECT + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                project = query.getProject(projId);
                m_projectId = project.getId();
            } catch (ProjectNotFoundException pnfe) {
                throw (ServletException)new ServletException
                    ("Project with id \"" + projId
                     + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + PROJECT + "\" not specified");
        }

        // Get faq.
        Faq faq = null;
        part = (Part)partList.get(FAQ);
        if (null != part && part.isParam()) {
            int faqId = -1;
            try {
                faqId = Integer.parseInt(((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + FAQ + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                faq = project.getFaq(faqId);
                m_faqId = faq.getId();
            } catch (FaqNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Faq with id \"" + faqId
                     + "\" not found").initCause(fnfe);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + FAQ + "\" not specified");
        }

        // Get question.
        Question question = null;
        part = (Part)partList.get(QUESTION);
        if (null != part && part.isParam()) {
            int questionId = -1;
            try {
                questionId = Integer.parseInt
                    (((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + QUESTION + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                question = faq.getQuestion(questionId);
                m_questionId = question.getId();
            } catch (QuestionNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Question with id \"" + questionId
                     + "\" not found").initCause(fnfe);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + QUESTION + "\" not specified");
        }

        // Get qstring.
        String qstring = null;
        part = (Part)partList.get(QSTRING);
        if (null != part && part.isParam()) {
            try {
                qstring = ((ParamPart)part).getStringValue();
                if (null != qstring) {
                    qstring = qstring.trim();
                }
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + QSTRING + "\" parameter could not be read")
                    .initCause(uee);
            }
            if (null == qstring || qstring.length() <= 0) {
                throw new EmptyQuestionException
                    ("\"" + QSTRING + "\" parameter is empty");
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + QSTRING + "\" not specified");
        }

        // Blindly overwrite whatever string for the question already
        // exists.
        question.setQuestion(qstring);
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
     * @param req contains the POST data used to modify the Question.
     * @param resp used to output HTTP response.
     * @throws ServletException if anything goes wrong, including
     * problems with the backend.
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // Get list of (parameter, value) pairs.
        HashMap paramList = parseParameters(req, SIXTYFOUR_KB);

        // Get handle on database.
        QueryDB query = null;
        try {
            query = new QueryDB(getServletContext());
        } catch (DatabaseNotFoundException dnfe) {
            serveError(req, resp, dnfe);
            return;
        }

        Exception editEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Edit record in back-end.
            editQuestion(query, paramList);

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
            if (editEx instanceof EmptyQuestionException) {
                Object[] ids = {
                    new Integer(m_projectId),
                    new Integer(m_faqId),
                    new Integer(m_questionId)
                };
                String path = MessageFormat.format(URL_NOQUESTION_ERROR, ids);
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
            Object[] ids = {
                new Integer(m_projectId), new Integer(m_faqId)
            };
            String path = MessageFormat.format(FORWARD_URL, ids);
            forwardRequest(query.getDb(), path, req, resp);
        }
    }
}
