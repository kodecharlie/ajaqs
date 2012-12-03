/**
 * Faq.java
 */

package com.beardediris.ajaqs.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain FAQs, each associated
 * with some project.</p>
 */
public class Faq
    implements Hideable, Serializable
{
    /*
     * Fields in Faq table. ID is the primary key, which
     * is auto-sequenced.  DO NOT set ID explicitly.
     */
    private int id;
    private String name;
    private Date creationDate;
    private int state;
    private Project project;

    /* Relational data for Faq. */
    private Collection questions;

    public Faq() {
        name = null;
        creationDate = null;
        state = UNDEFINED;
        questions = new ArrayList();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=");
        sb.append((null != name) ? name : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";state=").append(state);
        sb.append(";project=[").append(project).append("]");
        sb.append(";questions=[").append(questions).append("]");
        return sb.toString();
    }

    public boolean equals(Faq other) {
        return (id == other.getId());
    }

    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=");
        sb.append((null != name) ? name : "");
        sb.append(";project=[").append(project.getName()).append("]");
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

    public Collection getQuestions() {
        return questions;
    }
    public void setQuestions(Collection questions) {
        this.questions = questions;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    /**************************************************************
     * Following methods are accessors used to retrieve
     * information associated with each Faq.
     */

    public Question getQuestion(int qid)
        throws QuestionNotFoundException
    {
        Question question = null;
        Iterator it = questions.iterator();
        while (it.hasNext()) {
            Question cur = (Question)it.next();
            if (qid == cur.getId()) {
                question = cur;
                break;
            }
        }
        if (null == question) {
            throw new QuestionNotFoundException
                ("Question with id \"" + qid + "\" not found");
        }
        return question;
    }

    /**
     * Remove the named Question from the Faq's list of Questions.
     * Assume the transaction was initiated by the invoking code.
     *
     * @param questionId is the primary key of the <code>Faq</code>.
     * @return <code>Question</code> that was removed.
     */
    public Question removeQuestion(int questionId)
        throws QuestionNotFoundException
    {
        Question q = getQuestion(questionId);
        questions.remove(q);
        return q;
    }
}
