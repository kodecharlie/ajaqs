/**
 * FaqUser.java
 */

package com.beardediris.ajaqs.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain an FAQ user.</p>
 */
public class FaqUser
    implements Hideable, Serializable
{
    /* Fields in FaqUser table. */
    private String logon;
    private String password;
    private String email;
    private Date creationDate;
    private Date lastLoginDate;
    private int state;

    /* Relational collections for FaqUser. */
    private Collection projects;
    private Collection roles;

    public FaqUser() {
        logon = null;
        password = null;
        email = null;
        creationDate = null;
        lastLoginDate = null;
        state = UNDEFINED;
        projects = new ArrayList();
        roles = new ArrayList();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("logon=");
        sb.append((null != logon) ? logon : "");
        sb.append(";password=");
        sb.append((null != password) ? password : "");
        sb.append(";email=");
        sb.append((null != email) ? email : "");
        sb.append(";creationDate=");
        sb.append((null != creationDate) ? creationDate.toString() : "");
        sb.append(";lastLoginDate=");
        sb.append((null != lastLoginDate) ? lastLoginDate.toString() : "");
        sb.append(";state=").append(state);
        sb.append(";projects=[").append(projects).append("]");
        sb.append(";roles=[").append(roles).append("]");
        return sb.toString();
    }

    public boolean equals(FaqUser other) {
        return (logon.equals(other.getLogon()));
    }

    public int hashCode() {
        return logon.hashCode();
    }

    public String getLogon() {
        return logon;
    }
    public void setLogon(String logon) {
        this.logon = logon;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Collection getProjects() {
        return projects;
    }
    public void setProjects(Collection projects) {
        this.projects = projects;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public Collection getRoles() {
        return roles;
    }
    public void setRoles(Collection roles) {
        this.roles = roles;
    }

    /**************************************************************
     * Following methods are accessors used to retrieve
     * information associated with each FaqUser.
     */

    public Project getProject(int projId)
        throws ProjectNotFoundException
    {
        Project project = null;
        Iterator it = projects.iterator();
        while (it.hasNext()) {
            Project cur = (Project)it.next();
            if (projId == cur.getId()) {
                project = cur;
                break;
            }
        }
        if (null == project) {
            throw new ProjectNotFoundException
                ("Project \"" + projId + "\" not found");
        }
        return project;
    }

    /**
     * Remove the named project from the user's list of projects.
     * Assume the transaction was initiated by the invoking code.
     *
     * @param proj is the <code>Project</code> to be removed.
     * @return <code>true</code> if the project was removed, or
     * <code>false</code> otherwise.
     */
    public boolean removeProject(Project proj)
    {
        boolean removed = projects.remove(proj);
        return removed;
    }

    /**
     * Add the project to this user's list of projects.
     *
     * @return <code>true</code> if the list changed; <code>false</code>
     * otherwise.
     */
    public boolean addProject(Project p)
    {
        boolean rc = projects.add(p);
        return rc;
    }
}
