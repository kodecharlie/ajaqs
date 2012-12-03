/**
 * SubmitQuestion.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.EmptyQuestionException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
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
 * <p>This class implements a servlet used to create a question
 * for an existing FAQ.  See <tt>web.xml</tt> and
 * <tt>submitquestion.jsp</tt> for how this servlet is used.</p>
 */
public final class SubmitQuestion extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(SubmitQuestion.class.getName());

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
        "/jsp/user/browse.jsp?project={0}&faq={1}";

    /**
     * This URL is triggered when the question submitted is empty.
     */
    private final static String URL_NOQUESTION_ERROR =
        "/jsp/user/submitquestion.jsp?project={0}&faq={1}"
        + "&error=emptyquestion";

    /**
     * Transient fields.
     */
    private int m_projectId;
    private int m_faqId;

    /**
     * As a side-effect, this method sets member variables
     * m_projectId and m_faqId.
     */
    private void createQandA(String logon, QueryDB query, HashMap partList)
        throws ServletException, EmptyQuestionException
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
        String theQuestion = null;
        part = (Part)partList.get(QUESTION);
        if (null != part && part.isParam()) {
            try {
                theQuestion = ((ParamPart)part).getStringValue();
                if (null != theQuestion) {
                    theQuestion = theQuestion.trim();
                }
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + QUESTION + "\" parameter could not be read")
                    .initCause(uee);
            }
            if (null == theQuestion || theQuestion.length() <= 0) {
                throw new EmptyQuestionException
                    ("\"" + QUESTION + "\" parameter is empty");
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + QUESTION + "\" not specified");
        }

        // Create the question.
        logger.info("got question: " + theQuestion);
        Question question = new Question();
        question.setQuestion(theQuestion);
        question.setCreationDate(new Date());
        question.setFaq(faq);
        question.setUser(fuser);
        try {
            query.getDb().create(question);
        } catch (ClassNotPersistenceCapableException cnpce) {
            throw (ServletException)new ServletException
                ("Could not create new Question").initCause(cnpce);
        } catch (DuplicateIdentityException die) {
            throw (ServletException)new ServletException
                ("Could not create new Question").initCause(die);
        } catch (TransactionNotInProgressException tnipe) {
            throw (ServletException)new ServletException
                ("Could not create new Question").initCause(tnipe);
        } catch (PersistenceException pe) {
            throw (ServletException)new ServletException
                ("Could not create new Question").initCause(pe);
        }

        // Get optional answer.
        Answer answer = null;
        part = (Part)partList.get(ANSWER);
        if (null != part && part.isParam()) {
            String theAnswer = null;
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
            if (null != theAnswer && theAnswer.length() > 0) {
                answer = new Answer();
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
            }
        }

        if (null != answer) {
            ArrayList ansList = new ArrayList();
            ansList.add(answer);
            question.setAnswers(ansList);
        }
    }

    /**
     * Do not use this method.  New questions should be created via
     * POST.
     *
     * @throws ServletException always, because GET is not used to
     * create new questions.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        ServletException srvEx = new ServletException
            ("Cannot submit a new question with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * This method is called when a new question is created via an HTTP
     * POST command.  The backend work is processed as a Castor
     * transaction.  If any answer is submitted together with the
     * question, then a new answer will also be created, too.  When
     * the new records have been created in the backend, we forward
     * the user to the page given by the member variable
     * <code>FORWARD_URL</code>.
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

            // Create question and (optionally) an answer.
            createQandA(req.getRemoteUser(), query, paramList);

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
            new Integer(m_projectId), new Integer(m_faqId)
        };
        if (null != createEx) {
            if (createEx instanceof EmptyQuestionException) {
                String path = MessageFormat.format(URL_NOQUESTION_ERROR, ids);
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
