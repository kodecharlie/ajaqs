/**
 * AddAttachment.java
 */

package com.beardediris.ajaqs.util;

import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Attachment;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.AnswerNotFoundException;
import com.beardediris.ajaqs.ex.AttachmentNotFoundException;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.EmptyFilenameException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.MultipartParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This class implements a servlet used to add an attachment
 * to an existing answer.  See <tt>web.xml</tt> and
 * <tt>attach.jsp</tt> for how this servlet is used.</p>
 */
public final class AddAttachment extends Submitter
{
    private static final Logger logger =
        Logger.getLogger(AddAttachment.class.getName());

    /**
     * This is the size-limitation for files uploaded
     * to Ajaqs.  In MySql, this corresponds to the size
     * of a MEDIUMBLOB type.  This probably should be
     * an option, as it could be database-dependent.
     */
    private final static int SIXTEEN_MB = 16 * 1024 * 1024;

    /**
     * Servlet parameters.
     */
    private final static String PROJECT = "project";
    private final static String FAQ = "faq";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";
    private final static String FILENAME = "filename";
    private final static String FILETYPE = "filetype";
    private final static String DESCR = "descr";

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/user/answer.jsp?project={0}&faq={1}&question={2}";

    /**
     * This URL is triggered when the filename submitted is empty.
     */
    private final static String URL_NOFILENAME_ERROR =
        "/jsp/user/attach.jsp?project={0}&faq={1}&question={2}&answer={3}"
        + "&error=emptyfilename";

    /**
     * Transient fields used during upload.
     */
    private int m_projId;
    private int m_faqId;
    private int m_questionId;
    private int m_answerId;

    /**
     * This method returns a positive integer corresponding
     * to a newly created attachment, if everything proceeds
     * without error; otherwise, -1 is returned.
     */
    private int createAttachment(Database db, FilePart part)
        throws ServletException
    {
        int id = -1;

        Attachment attachment = new Attachment();
        attachment.setCreationDate(new Date());
        attachment.setFileName(part.getFileName());
        attachment.setAttachment(part.getInputStream());

        try {
            // Begin transaction.
            db.begin();

            db.create(attachment);
            id = attachment.getId();
            logger.info("created attachment: " + id);

            // End transaction.
            db.commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            try {
                // Rollback the transaction.
                db.rollback();
            } catch (TransactionNotInProgressException tnpe) {
                // We do not care.
                logger.info("could not rollback transaction: " + tnpe);
            }

            // The database connection is closed by the invoking code,
            // even in the event of an exception.
            if (! (ex instanceof ServletException)) {
                ex = (ServletException)new ServletException
                    ("Could not add attachment").initCause(ex);
            }
            throw (ServletException)ex;
        }

        return id;
    }

    private void tackonExtras(QueryDB query, int attachId,
                              HashMap partList, String logon)
        throws ServletException, EmptyFilenameException
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
            try {
                m_projId = Integer.parseInt
                    (((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + PROJECT + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                project = fuser.getProject(m_projId);
            } catch (ProjectNotFoundException pnfe) {
                throw (ServletException)new ServletException
                    ("Project with id \"" + m_projId
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
            try {
                m_faqId = Integer.parseInt
                    (((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + FAQ + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                faq = project.getFaq(m_faqId);
            } catch (FaqNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Faq with id \"" + m_faqId
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
            try {
                m_questionId = Integer.parseInt
                    (((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + QUESTION + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                question = faq.getQuestion(m_questionId);
            } catch (QuestionNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Question with id \"" + m_questionId
                     + "\" not found").initCause(fnfe);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + QUESTION + "\" not specified");
        }

        // Get answer.
        Answer answer = null;
        part = (Part)partList.get(ANSWER);
        if (null != part && part.isParam()) {
            try {
                m_answerId = Integer.parseInt
                    (((ParamPart)part).getStringValue());
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + ANSWER + "\" parameter could not be read")
                    .initCause(uee);
            }
            try {
                answer = question.getAnswer(m_answerId);
            } catch (AnswerNotFoundException fnfe) {
                throw (ServletException)new ServletException
                    ("Answer with id \"" + m_answerId
                     + "\" not found").initCause(fnfe);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + ANSWER + "\" not specified");
        }

        // We are done parsing IDs from the parameter list. Check
        // if the filename is empty.  This condition is true iff the
        // attachId is < 0.
        if (attachId < 0) {
            throw new EmptyFilenameException
                ("\"" + FILENAME + "\" parameter is empty");
        }

        // Get filetype.
        String fileType = null;
        part = (Part)partList.get(FILETYPE);
        if (null != part && part.isParam()) {
            try {
                fileType = ((ParamPart)part).getStringValue();
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + FILETYPE + "\" parameter could not be read")
                    .initCause(uee);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + FILETYPE + "\" not specified");
        }

        // Get description of attachment.
        String descr = null;
        part = (Part)partList.get(DESCR);
        if (null != part && part.isParam()) {
            try {
                descr = ((ParamPart)part).getStringValue();
            } catch (UnsupportedEncodingException uee) {
                throw (ServletException)new ServletException
                    ("\"" + DESCR + "\" parameter could not be read")
                    .initCause(uee);
            }
        } else {
            throw new ServletException
                ("Servlet parameter \"" + DESCR + "\" not specified");
        }

        // Get attachment.
        Attachment attachment = null;
        try {
            attachment = query.getAttachment(attachId);
        } catch (AttachmentNotFoundException anfe) {
            throw (ServletException)new ServletException
                ("Attachment with id \"" + attachId
                 + "\" not found").initCause(anfe);
        }

        // Fill in missing attributes for attachment.
        attachment.setUser(fuser);
        attachment.setAnswer(answer);
        attachment.setFileType(fileType);
        attachment.setDescr(descr);
    }

    private void upload(HttpServletRequest req, QueryDB query)
        throws ServletException, IOException, EmptyFilenameException
    {
        int attachId = -1;
        HashMap partList = new HashMap();
        MultipartParser mp = new MultipartParser(req, SIXTEEN_MB);
        do {
            Part part = part = mp.readNextPart();
            if (null == part) {
                break;
            }

            // If the Part is a file, read it in now and create
            // the attachment before proceeding to the next part.
            // The task of actually reading the input file data
            // occurs in the background when the record for the
            // attachment is created in the backend.  NOTE: we
            // do not bail out here if the filename is empty.
            // Instead, we bail out later only after first parsing
            // the rest of the MIME parts for IDs for the project,
            // faq, question, and answer, which in turn are used to
            // forward the end-user to JSP that describes the
            // problem.

            if (part.getName().equals(FILENAME)) {
                if (part.isFile()) {
                    FilePart fp = (FilePart)part;
                    String filename = fp.getFileName();
                    logger.info("got filename=" + filename);
                    if (null != filename && filename.length() > 0) {
                        attachId = createAttachment(query.getDb(), fp);
                    }
                } else {
                    throw new ServletException
                        ("Servlet parameter \"" + FILENAME + "\" must "
                         + "contain a file attachment");
                }
            } else {
                partList.put(part.getName(), part);
            }
        } while (true);

        try {
            // Begin transaction.
            query.getDb().begin();

            // Tack on additional information for new attachment.
            tackonExtras(query, attachId, partList, req.getRemoteUser());

            // End transaction.
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            try {
                // Rollback the transaction.
                query.getDb().rollback();
            } catch (TransactionNotInProgressException tnpe) {
                // We do not care.
                logger.info("could not rollback transaction: " + tnpe);
            }
            if (ex instanceof ServletException) {
                throw (ServletException)ex;
            } else if (ex instanceof IOException) {
                throw (IOException)ex;
            } else if (ex instanceof EmptyFilenameException) {
                throw (EmptyFilenameException)ex;
            } else {
                ex = (ServletException)new ServletException
                    ("Could not add attachment").initCause(ex);
            }
        }
    }

    /**
     * Do not use this method.  New attachments should be uploaded
     * via POST.
     *
     * @throws ServletException always, because GET is not used to
     * upload new attachments.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        ServletException srvEx = new ServletException
            ("Cannot upload a new attachment with GET.");
        serveError(req, resp, srvEx);
    }

    /**
     * This method is called when a new attachment is uploaded via an
     * HTTP POST command. After the attachment has been created in
     * the backend, we forward the user to the page given by
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
        // Get handle on database.
        QueryDB query = null;
        try {
            query = new QueryDB(getServletContext());
        } catch (DatabaseNotFoundException dnfe) {
            serveError(req, resp, dnfe);
            return;
        }

        // Upload attachment.
        Exception uploadEx = null;
        try {
            upload(req, query);
        } catch (Exception ex) {
            uploadEx = ex;
        } finally {
            // Close the database below.  See comment in Register.java.
        }

        if (null != uploadEx) {
            if (uploadEx instanceof EmptyFilenameException) {
                Object[] ids = {
                    new Integer(m_projId), new Integer(m_faqId),
                    new Integer(m_questionId), new Integer(m_answerId)
                };
                String path = MessageFormat.format(URL_NOFILENAME_ERROR, ids);
                forwardRequest(query.getDb(), path, req, resp);
            } else {
                // Close the database.
                try {
                    query.getDb().close();
                } catch (PersistenceException pe) {
                    // See comment in Register.java.
                }
                serveError(req, resp, uploadEx);
            }
        } else {
            // Forward end-user to JSP showing FAQ just modified.
            Object[] ids = {
                new Integer(m_projId), new Integer(m_faqId),
                new Integer(m_questionId)
            };
            String path = MessageFormat.format(FORWARD_URL, ids);
            forwardRequest(query.getDb(), path, req, resp);
        }
    }
}
