/**
 * Answer.java
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
import com.beardediris.ajaqs.ex.AttachmentNotFoundException;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain answers to questions.</p>
 */
public class Answer
    implements Serializable
{
    /*
     * Fields in Answer table. ID is the primary key, which
     * is auto-sequenced.  DO NOT set ID explicitly.
     */
    private int id;
    private InputStream astream;
    private String answer;        // Read in from astream.
    private Date creationDate;
    private Question question;
    private FaqUser user;

    /* Relational data for Answer. */
    private Collection attachments;

    public Answer() {
        astream = null;
        answer = null;
        creationDate = null;
        question = null;
        user = null;
        attachments = new ArrayList();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("answer=");
        sb.append((null != answer) ? answer : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";question=[").append(question).append("]");
        sb.append(";user=[").append(user).append("]");
        sb.append(";attachments=[").append(attachments).append("]");
        return sb.toString();
    }

    public boolean equals(Answer other) {
        return (id == other.getId());
    }

    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(";answer=");
        sb.append((null != answer) ? answer : "");
        sb.append(";question=[").append(question.getQuestion()).append("]");
        return sb.toString().hashCode();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public InputStream getAstream() {
        return astream;
    }
    public void setAstream(InputStream astream) {
        this.astream = astream;
    }

    public String getAnswer() {
        // Read qstream on-demand.
        if (null == answer) {
            // This is a sanity-check. astream must be set.
            if (null == astream) {
                throw new RuntimeException("InputStream is null");
            }

            BufferedInputStream is = new BufferedInputStream(astream);
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
                sb.append(new String(inbytes, 0, nread));
            }
            answer = sb.toString();

            // Close stream because we read the answer.
            try {
                is.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Error closing stream", ioe);
            }
        }
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;

        // Reset the astream.  This means that if the entity
        // is committed to the database, then the stream will
        // contain the new answer.
        if (null == answer) {
            astream = new ByteArrayInputStream("".getBytes());
        } else {
            astream = new ByteArrayInputStream(answer.getBytes());
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public FaqUser getUser() {
        return user;
    }
    public void setUser(FaqUser user) {
        this.user = user;
    }

    public Collection getAttachments() {
        return attachments;
    }
    public void setAttachments(Collection attachments) {
        this.attachments = attachments;
    }

    /**************************************************************
     * Following methods are accessors used to retrieve
     * information associated with each Answer.
     */

    public Attachment getAttachment(int atchId)
        throws AttachmentNotFoundException
    {
        Attachment attachment = null;
        Iterator it = attachments.iterator();
        while (it.hasNext()) {
            Attachment cur = (Attachment)it.next();
            if (atchId == cur.getId()) {
                attachment = cur;
                break;
            }
        }
        if (null == attachment) {
            throw new AttachmentNotFoundException
                ("Attachment with id \"" + atchId + "\" not found");
        }
        return attachment;
    }

    public Attachment getLastAttachment()
        throws AttachmentNotFoundException
    {
        Attachment last = null;
        Iterator it = attachments.iterator();
        while (it.hasNext()) {
            Attachment cur = (Attachment)it.next();
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
            throw new AttachmentNotFoundException
                ("No attachment found for Answer with id \"" + id + "\"");
        }
        return last;
    }

    /**
     * Remove the named Attachment from the Answer's list of Attachments.
     * Assume the transaction was initiated by the invoking code.
     *
     * @param attachmentId is the primary key of the <code>Attachment</code>.
     * @return <code>Attachment</code> that was removed.
     */
    public Attachment removeAttachment(int attachmentId)
        throws AttachmentNotFoundException
    {
        Attachment a = getAttachment(attachmentId);
        attachments.remove(a);
        return a;
    }
}
