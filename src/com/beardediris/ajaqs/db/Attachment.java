/**
 * Attachment.java
 */

package com.beardediris.ajaqs.db;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain attachments, each associated
 * with some answer.</p>
 */
public class Attachment
    implements Serializable
{
    /*
     * Fields in Attachment table. ID is the primary key, which
     * is auto-sequenced.  DO NOT set ID explicitly.
     */
    private int id;
    private InputStream attachment;
    private String descr;
    private Date creationDate;
    private String fileName;
    private String fileType;
    private Answer answer;
    private FaqUser user;

    public Attachment() {
        attachment = null;
        descr = null;
        creationDate = null;
        fileName = null;
        fileType = null;
        answer = null;
        user = null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("descr=");
        sb.append((null != descr) ? descr : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";fileName=");
        sb.append((null != fileName) ? fileName : "");
        sb.append(";fileType=");
        sb.append((null != fileType) ? fileType : "");
        sb.append(";answer=[").append(answer).append("]");
        sb.append(";user=[").append(user).append("]");
        return sb.toString();
    }

    public boolean equals(Attachment other) {
        return (id == other.getId());
    }

    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(";descr=");
        sb.append((null != descr) ? descr : "");
        sb.append(";fileName=");
        sb.append((null != fileName) ? fileName : "");
        sb.append(";fileType=");
        sb.append((null != fileType) ? fileType : "");
        sb.append(";answer=[").append(answer.getAnswer()).append("]");
        sb.append(";user=[").append(user.getLogon()).append("]");
        return sb.toString().hashCode();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public InputStream getAttachment() {
        return attachment;
    }
    public void setAttachment(InputStream attachment) {
        this.attachment = attachment;
    }

    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Answer getAnswer() {
        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public FaqUser getUser() {
        return user;
    }
    public void setUser(FaqUser user) {
        this.user = user;
    }
}
