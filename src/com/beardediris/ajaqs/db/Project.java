/**
 * Project.java
 */

package com.beardediris.ajaqs.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import com.beardediris.ajaqs.ex.FaqNotFoundException;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain projects.</p>
 */
public class Project
    implements Hideable, Serializable
{
    /*
     * Fields in Project table. ID is the primary key, which
     * is auto-sequenced.  DO NOT set ID explicitly.
     */
    private int id;
    private String name;
    private Date creationDate;
    private int state;

    /* Relational data for Project. */
    private Collection faqs;

    public Project() {
        name = null;
        creationDate = null;
        state = UNDEFINED;
        faqs = new ArrayList();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=");
        sb.append((null != name) ? name : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";state=").append(state);
        return sb.toString();
    }

    public boolean equals(Project other) {
        return (id == other.getId());
    }

    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(";name=");
        sb.append((null != name) ? name : "");
        return sb.toString().hashCode();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public Collection getFaqs() {
        return faqs;
    }
    public void setFaqs(Collection faqs) {
        this.faqs = faqs;
    }

    /**************************************************************
     * Following methods are accessors used to retrieve
     * information associated with each Project.
     */

    public Faq getFaq(int faqId)
        throws FaqNotFoundException
    {
        Faq faq = null;
        Iterator it = faqs.iterator();
        while (it.hasNext()) {
            Faq cur = (Faq)it.next();
            if (faqId == cur.getId()) {
                faq = cur;
                break;
            }
        }
        if (null == faq) {
            throw new FaqNotFoundException
                ("Faq with id \"" + faqId + "\" not found");
        }
        return faq;
    }

    /**
     * Remove the named Faq from the project's list of Faqs.
     * Assume the transaction was initiated by the invoking code.
     *
     * @param faqId is the primary key of the <code>Faq</code>.
     * @return <code>Faq</code> that was removed.
     */
    public Faq removeFaq(int faqId)
        throws FaqNotFoundException
    {
        Faq f = getFaq(faqId);
        faqs.remove(f);
        return f;
    }

    /**
     * Add the FAQ to this project's list of FAQs.
     *
     * @return <code>true</code> if the list changed; <code>false</code>
     * otherwise.
     */
    public boolean addFaq(Faq f)
    {
        boolean rc = faqs.add(f);
        return rc;
    }
}
