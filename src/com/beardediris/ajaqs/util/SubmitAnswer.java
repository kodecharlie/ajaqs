/**
 * SubmitAnswer.java
 */

package com.beardediris.ajaqs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.EmptyAnswerException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.oql.QueryDB;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This class implements a servlet used to create a new
 * answer for an existing question.  See <tt>web.xml</tt> and
 * <tt>submitanswer.jsp</tt> for how this servlet is used.</p>
 *
 * @see Submitter
 */
public final class SubmitAnswer extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(SubmitAnswer.class.getName());

    /**
     * This is the size-limitation for answers uploaded
     * to Ajaqs.  In MySql, this corresponds to the size
     * of a BLOB type.  This probably should be an option,
     * as it could be database-dependent.
     */
    private final static int SIXTYFOUR_KB = 64 * 1024;

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/user/answer.jsp?project={0}&faq={1}&question={2}";

    /**
     * This URL is triggered when the answer submitted is empty.
     */
    private final static String URL_NOANSWER_ERROR =
        "/jsp/user/submitanswer.jsp?project={0}&faq={1}&question={2}"
        + "&error=emptyanswer";

    /**
     * These member variables are transient.
     */
    private int m_projectId;
    private int m_faqId;
    private int m_questionId;

    /**
     * As a side-effect, this method sets member variables
     * m_projectId, m_faqId, and m_questionId.
     */
    private void createAnswer(String logon, QueryDB query, HashMap partList)
        throws ServletException, EmptyAnswerException
    {
        // Get user.
        FaqUser fuser = null;
        if (null != logon && logon.length() > 0) {
            try {
                fuser = query.getFaqUser(logon, false);
            } catch (UserNotFoundException unfe) {
                throw (ServletException)new ServletException
                    ("FaqUser with logon \"" + logon
                     + "\" not found").initCause(unfe);
            }
        } else {
            throw new ServletException("Remote user not authenticated");
        }

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
                project = fuser.getProject(projId);
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

        // Get answer.
        String theAnswer = null;
        part = (Part)partList.get(ANSWER);
        if (null != part && part.isParam()) {
            try {
                theAnswer = ((ParamPart)part).getStringValue();
                if (null != theAnswer) {
                    theAnswer = theAnswer.trim();
                }
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + ANSWER + "\" parameter could not be read")
                    .initCause(uee);
            }
            if (null == theAnswer || theAnswer.length() <= 0) {
                throw new EmptyAnswerException
                    ("\"" + ANSWER + "\" parameter is empty");
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + ANSWER + "\" not specified");
        }

        // Create the answer.
        Answer answer = new Answer();
        answer.setAnswer(theAnswer);
        answer.setCreationDate(new Date());
        answer.setQuestion(question);
        answer.setUser(fuser);
        try {
            query.getDb().create(answer);
        } catch (ClassNotPersistenceCapableException cnpce) {
            throw (ServletException)new ServletException
                ("Could not create new Answer").initCause(cnpce);
        } catch (DuplicateIdentityException die) {
            throw (ServletException)new ServletException
                ("Could not create new Answer").initCause(die);
        } catch (TransactionNotInProgressException tnipe) {
            throw (ServletException)new ServletException
                ("Could not create new Answer").initCause(tnipe);
        } catch (PersistenceException pe) {
            throw (ServletException)new ServletException
                ("Could not create new Answer").initCause(pe);
        }

        // Add answer to question.
        Collection ansList = question.getAnswers();
        ansList.add(answer);
        question.setAnswers(ansList);
    }

    /**
     * Do not use this method.  New answers should be created via
     * POST.
     *
     * @throws ServletException always, because GET is not used to
     * create new answers.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        ServletException srvEx = new ServletException
            ("Cannot submit a new answer with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * This method is called when a new answer is created via an HTTP
     * POST command.  The backend work is processed as a Castor
     * transaction.  When the new record has been created in the
     * backend, we forward the user to the page given by the member
     * variable <code>FORWARD_URL</code>.
     *
     * @param req contains request-data submitted in POST.
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

        Exception createEx = null;
        try {
            // Begin transaction.
            query.getDb().begin();

            // Create answer.
            createAnswer(req.getRemoteUser(), query, paramList);

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

        // Use these parameters in calls to forwardRequest below.
        Object[] ids = {
            new Integer(m_projectId),
            new Integer(m_faqId),
            new Integer(m_questionId)
        };
        if (null != createEx) {
            if (createEx instanceof EmptyAnswerException) {
                String path = MessageFormat.format(URL_NOANSWER_ERROR, ids);
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
