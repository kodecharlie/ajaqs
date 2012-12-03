/**
 * Question.java
 */

package com.beardediris.ajaqs.db;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.ex.AnswerNotFoundException;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain questions.</p>
 */
public class Question
    implements Serializable
{
    private static final Logger logger =
        Logger.getLogger(Question.class.getName());

    /*
     * Fields in Question table. ID is the primary key, which
     * is auto-sequenced.  DO NOT set ID explicitly.
     */
    private int id;
    private InputStream qstream;
    private String question;      // Read in from qstream.
    private Date creationDate;
    private Faq faq;
    private FaqUser user;

    /* Relational data for Question. */
    private Collection answers;

    public Question() {
        qstream = null;
        question = null;
        creationDate = null;
        answers = null;
        faq = null;
        user = null;
        answers = new ArrayList();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("question=");
        sb.append((null != question) ? question : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";faq=[").append(faq).append("]");
        sb.append(";user=[").append(user).append("]");
        sb.append(";answers=[").append(answers).append("]");
        return sb.toString();
    }

    public boolean equals(Question other) {
        return (id == other.getId());
    }

    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(";question=");
        sb.append((null != question) ? question : "");
        sb.append(";faq=[").append(faq).append("]");
        sb.append(";user=[").append(user).append("]");
        return sb.toString().hashCode();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public InputStream getQstream() {
        return qstream;
    }
    public void setQstream(InputStream qstream) {
        this.qstream = qstream;
    }

    public String getQuestion() {
        // Read qstream on-demand.
        if (null == question) {
            // This is a sanity-check. qstream must be set.
            if (null == qstream) {
                throw new RuntimeException("InputStream is null");
            }

            logger.info("Reading stream to get question");
            BufferedInputStream is = new BufferedInputStream(qstream);
            StringBuffer sb = new StringBuffer();
            byte[] inbytes = new byte[512];
            while (true) {
                int nread;
                try {
                    nread = is.read(inbytes, 0, 512);
                } catch (IOException ioe) {
                    throw new RuntimeException("Error reading stream", ioe);
                }
                if (nread < 0) {
                    break;
                }
                logger.info("nread=" + nread);
                sb.append(new String(inbytes, 0, nread));
            }
            question = sb.toString();
            logger.info("question=" + question);

            // Close stream because we read the question.
            try {
                is.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Error closing stream", ioe);
            }
        }
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;

        // Reset the qstream.  This means that if the entity
        // is committed to the database, then the stream will
        // contain the new question.
        if (null == question) {
            qstream = new ByteArrayInputStream("".getBytes());
        } else {
            qstream = new ByteArrayInputStream(question.getBytes());
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Collection getAnswers() {
        return answers;
    }
    public void setAnswers(Collection answers) {
        this.answers = answers;
    }

    public Faq getFaq() {
        return faq;
    }
    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public FaqUser getUser() {
        return user;
    }
    public void setUser(FaqUser user) {
        this.user = user;
    }

    /**************************************************************
     * Following methods are accessors used to retrieve
     * information associated with each Question.
     */

    public Answer getAnswer(int ansId)
        throws AnswerNotFoundException
    {
        Answer answer = null;
        Iterator it = answers.iterator();
        while (it.hasNext()) {
            Answer cur = (Answer)it.next();
            if (ansId == cur.getId()) {
                answer = cur;
                break;
            }
        }
        if (null == answer) {
            throw new AnswerNotFoundException
                ("Answer with id \"" + ansId + "\" not found");
        }
        return answer;
    }

    public Answer getLastAnswer()
        throws AnswerNotFoundException
    {
        Answer last = null;
        Iterator it = answers.iterator();
        while (it.hasNext()) {
            Answer cur = (Answer)it.next();
            if (null != last) {
                if (cur.getCreationDate().getTime()
                    > last.getCreationDate().getTime()) {
                    last = cur;
                }
            } else {
                last = cur;
            }
        }
        if (null == last) {
            throw new AnswerNotFoundException
                ("No answer found for Question with id \"" + id + "\"");
        }
        return last;
    }

    /**
     * Remove the named Answer from the Question's list of Answers.
     * Assume the transaction was initiated by the invoking code.
     *
     * @param answerId is the primary key of the <code>Answer</code>.
     * @return <code>Answer</code> that was removed.
     */
    public Answer removeAnswer(int answerId)
        throws AnswerNotFoundException
    {
        Answer ans = getAnswer(answerId);
        answers.remove(ans);
        return ans;
    }
}
