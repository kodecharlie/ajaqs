/**
 * QueryDB.java
 */

package com.beardediris.ajaqs.oql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Attachment;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Role;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.AttachmentNotFoundException;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.RoleNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.session.Constants;
import com.beardediris.ajaqs.util.Util;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>QueryDB is used to access data from the back-end.  All
 * explicit SELECT commands should be placed here.</p>
Database:
    public static final short ReadOnly = 0;
    public static final short Shared = 1;
    public static final short Exclusive = 2;
    public static final short DbLocked = 3;
 */
public class QueryDB implements Constants
{
    private static final String ELLIPSIS = "...";
    private static final int MATCH_SIZE = 128;
    private static final Logger logger =
        Logger.getLogger(QueryDB.class.getName());

    private Database db;

    /***********************************************************
     * Private methods.
     ***********************************************************/

    /**
     * The "matching" string for a Question is simply the first
     * MATCH_SIZE characters of the first Answer (for that Question),
     * or the entire Answer - whichever is shorter.  We pad the end
     * of the matching string with an ellipsis if the Answer is more
     * than MATCH_SIZE characters.  In the event that no Answer has
     * been submitted for this Question, the matching string is empty.
     */
    private String getMatch(Question question) {
        StringBuffer match = new StringBuffer();
        Collection ansList = question.getAnswers();
        if (ansList.size() > 0) {
            Answer a = (Answer)ansList.iterator().next();
            String answer = a.getAnswer();
            if (answer.length() > MATCH_SIZE) {
                match.append(answer.substring(0, MATCH_SIZE));
                match.append(ELLIPSIS);
            } else {
                match.append(answer.substring(0));
            }
        }
        return match.toString();
    }

    /**
     * Return the "matching" string for an Answer.  This is just
     * the text surrounding the first matching keyword within the
     * answer.  We try to center the text optimally around the
     * index where the match occurs.
     */
    private String getMatch(Answer a, int index) {
        StringBuffer match = new StringBuffer();
        String answer = a.getAnswer();
        logger.info("index=" + index);
        if (index < (MATCH_SIZE / 2)) {
            int end = index + MATCH_SIZE - 1;
            if (answer.length() > end) {
                match.append(answer.substring(0, MATCH_SIZE));
                match.append(ELLIPSIS);
            } else {
                match.append(answer.substring(0));
            }
        } else {
            int start = index - (MATCH_SIZE / 2);
            int end = start + MATCH_SIZE - 1;
            logger.info("start=" + start + ";end=" + end);
            match.append(ELLIPSIS);
            if ((answer.length() - 1) > end) {
                match.append(answer.substring(start, MATCH_SIZE));
                match.append(ELLIPSIS);
            } else {
                match.append(answer.substring(start));
            }
        }
        return match.toString();
    }

    /**
     * For each Faq, search through all questions.
     * A match means one of two things:
     * 1. The question contains text that matches at least one
     *    of the keywords.
     * 2. One of the answers supplied to the question contains
     *    text that matches at least one of the keywords.
     * In either case, we create a SearchMatch, and add
     * it to our running list of matches.
     */
    private Collection searchFaq(Faq faq, Collection kwList)
    {
        // Set up our pattern-matcher.
        String regex = Util.getRegex(kwList);
        Pattern pat = Pattern.compile(regex);
        logger.info("regex: " + regex);

        // Cycle through all questions.
        ArrayList matches = new ArrayList();
        Iterator questIt = faq.getQuestions().iterator();
        while (questIt.hasNext()) {
            Question q = (Question)questIt.next();
            String question = q.getQuestion();
            Matcher mat = pat.matcher(question);
            if (mat.find()) {
                // Add match to our running list and continue
                // to the next question.
                SearchMatch sm = new SearchMatch(q, getMatch(q));
                matches.add(sm);
                continue;
            }

            // The question does not match, so search through answers.
            Iterator ansIt = q.getAnswers().iterator();
            while (ansIt.hasNext()) {
                Answer a = (Answer)ansIt.next();
                String answer = a.getAnswer();
                mat.reset(answer);
                if (mat.find()) {
                    String match = getMatch(a, mat.start());
                    SearchMatch sm = new SearchMatch(q, match);
                    matches.add(sm);
                    break;
                }
            }
        }
        return matches;
    }

    /***********************************************************
     * Public methods.
     ***********************************************************/

    /**
     * Find the <code>Database</code> associated with the
     * given <code>ServletContext</code>.  All OQL queries that
     * are initiated from the <code>QueryDB</code> object
     * will rely on this connection.  This constructor is
     * typically called from custom servlets provided by Ajaqs.
     */
    public QueryDB(ServletContext sctx)
        throws DatabaseNotFoundException
    {
        JDOManager jdo = (JDOManager)sctx.getAttribute(JDO_ATTR);
        if (null == jdo) {
            throw new DatabaseNotFoundException
                ("JDO not found in application context");
        }

        db = null;
        try {
            db = jdo.getDatabase();
        } catch (org.exolab.castor.jdo.DatabaseNotFoundException dnfe) {
            throw (DatabaseNotFoundException)new DatabaseNotFoundException
                ("Could not get Database from JDO").initCause(dnfe);
        } catch (PersistenceException pe) {
            throw (DatabaseNotFoundException)new DatabaseNotFoundException
                ("Could not get Database from JDO").initCause(pe);
        }
        if (null == db) {
            throw new DatabaseNotFoundException
                ("Database not found in application context");
        }
    }

    /**
     * Find the <code>Database</code> stored in the servlet
     * request context associated with the given <code>PageContext</code>.
     * This constructor is typically called from custom JSP tags
     * provided by Ajaqs.
     */
    public QueryDB(PageContext pctx)
        throws DatabaseNotFoundException
    {
        db = (Database)pctx.getRequest().getAttribute(DB_ATTR);
        if (null == db) {
            throw new DatabaseNotFoundException
                ("Database not found in request context");
        }
    }

    /**
     * @return <code>FaqUser</code> for the given <code>logon</code>.
     */
    public FaqUser getFaqUser(String logon, boolean readOnly)
        throws UserNotFoundException
    {
        FaqUser fuser = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
            sb.append("WHERE u.logon = $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind(logon);

            QueryResults results = null;
            if (readOnly) {
                results = oql.execute(Database.ReadOnly);
            } else {
                // Use default access mode, as specified in the
                // mapping file.
                results = oql.execute();
            }
            if (results.hasMore()) {
                fuser = (FaqUser)results.next();
            } else {
                throw new UserNotFoundException
                    ("Could not find user \"" + logon + "\"");
            }
        } catch (QueryException qe) {
            logger.info("qe: " + qe);
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not find user \"" + logon + "\"").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            logger.info("tnpe: " + tnpe);
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not find user \"" + logon + "\"").initCause(tnpe);
        } catch (PersistenceException pe) {
            logger.info("pe: " + pe);
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not find user \"" + logon + "\"").initCause(pe);
        }
        return fuser;
    }

    /**
     * Find and return all <code>FaqUser</code>s.
     *
     * @return <code>Collection</code>.
     */
    public Collection getAllFaqUsers(boolean readOnly)
        throws UserNotFoundException
    {
        ArrayList users = new ArrayList();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
            sb.append("WHERE u.logon LIKE $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind("%");

            QueryResults results = null;
            if (readOnly) {
                results = oql.execute(Database.ReadOnly);
            } else {
                // Use default access mode, as specified in the
                // mapping file.
                results = oql.execute();
            }
            while (results.hasMore()) {
                FaqUser fuser = (FaqUser)results.next();
                users.add(fuser);
            }
        } catch (QueryException qe) {
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not get all users").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not get all users").initCause(tnpe);
        } catch (PersistenceException pe) {
            throw (UserNotFoundException)new UserNotFoundException
                ("Could not get all users").initCause(pe);
        }
        return users;
    }

    /**
     * Find and return all <code>Project</code>s.
     *
     * @return <code>Collection</code>.
     */
    public Collection getAllProjects()
        throws ProjectNotFoundException
    {
        ArrayList projects = new ArrayList();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT p FROM com.beardediris.ajaqs.db.Project p ");
            sb.append("WHERE p.id LIKE $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind("%");

            QueryResults results = oql.execute();
            while (results.hasMore()) {
                Project p = (Project)results.next();
                projects.add(p);
            }
        } catch (QueryException qe) {
            logger.info("qe: " + qe);
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not get all projects").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            logger.info("tnpe: " + tnpe);
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not get all projects").initCause(tnpe);
        } catch (PersistenceException pe) {
            logger.info("pe: " + pe);
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not get all projects").initCause(pe);
        }
        return projects;
    }

    /**
     * @return <code>Project</code> for the given <code>id</code>.
     */
    public Project getProject(int id)
        throws ProjectNotFoundException
    {
        Project proj = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT p FROM com.beardediris.ajaqs.db.Project p ");
            sb.append("WHERE p.id = $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind(id);

            QueryResults results = oql.execute();
            if (results.hasMore()) {
                proj = (Project)results.next();
            } else {
                throw new ProjectNotFoundException
                    ("Could not find project \"" + id + "\"");
            }
        } catch (QueryException qe) {
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not find project \"" + id + "\"").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not find project \"" + id + "\"").initCause(tnpe);
        } catch (PersistenceException pe) {
            throw (ProjectNotFoundException)new ProjectNotFoundException
                ("Could not find project \"" + id + "\"").initCause(pe);
        }
        return proj;
    }

    /**
     * Find and return all <code>Role</code>s.
     *
     * @return <code>Collection</code>.
     */
    public Collection getAllRoles()
        throws RoleNotFoundException
    {
        ArrayList roles = new ArrayList();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT r FROM com.beardediris.ajaqs.db.Role r ");
            sb.append("WHERE r.roleName LIKE $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind("%");

            QueryResults results = oql.execute();
            while (results.hasMore()) {
                Role r = (Role)results.next();
                roles.add(r);
            }
        } catch (QueryException qe) {
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not get all roles").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not get all roles").initCause(tnpe);
        } catch (PersistenceException pe) {
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not get all roles").initCause(pe);
        }
        return roles;
    }

    /**
     * @return <code>Role</code> for the given <code>id</code>.
     */
    public Role getRole(String roleName)
        throws RoleNotFoundException
    {
        Role role = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT r FROM com.beardediris.ajaqs.db.Role r ");
            sb.append("WHERE r.roleName = $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind(roleName);

            QueryResults results = oql.execute();
            if (results.hasMore()) {
                role = (Role)results.next();
            } else {
                throw new RoleNotFoundException
                    ("Could not find role \"" + roleName + "\"");
            }
        } catch (QueryException qe) {
            logger.info("qe: " + qe);
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not find role \"" + roleName + "\"").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            logger.info("tnpe: " + tnpe);
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not find role \"" + roleName + "\"").initCause(tnpe);
        } catch (PersistenceException pe) {
            logger.info("pe: " + pe);
            throw (RoleNotFoundException)new RoleNotFoundException
                ("Could not find role \"" + roleName + "\"").initCause(pe);
        }
        return role;
    }

    /**
     * @return <code>Attachment</code> for the given <code>id</code>.
     */
    public Attachment getAttachment(int id)
        throws AttachmentNotFoundException
    {
        Attachment attachment = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT a FROM com.beardediris.ajaqs.db.Attachment a ");
            sb.append("WHERE a.id = $1");

            OQLQuery oql = db.getOQLQuery(sb.toString());
            oql.bind(id);

            QueryResults results = oql.execute();
            if (results.hasMore()) {
                attachment = (Attachment)results.next();
            } else {
                throw new AttachmentNotFoundException
                    ("Could not find attachment \"" + id + "\"");
            }
        } catch (QueryException qe) {
            throw (AttachmentNotFoundException)new AttachmentNotFoundException
                ("Could not find attachment \"" + id + "\"").initCause(qe);
        } catch (TransactionNotInProgressException tnpe) {
            throw (AttachmentNotFoundException)new AttachmentNotFoundException
                ("Could not find attachment \"" + id + "\"").initCause(tnpe);
        } catch (PersistenceException pe) {
            throw (AttachmentNotFoundException)new AttachmentNotFoundException
                ("Could not find attachment \"" + id + "\"").initCause(pe);
        }
        return attachment;
    }

    /**
     * This routine performs a search across the text of all
     * questions and answers.  More restrictive searches are
     * done elsewhere.  The current implementation does not
     * use OQL queries, but we leave this routine here just in
     * case a future implementation might choose to access the
     * <code>Database</code> directly via OQL.
     *
     * @return <code>Collection</code> of <code>SearchMatch</code>
     * objects.
     */
    public Collection findMatches
        (FaqUser fuser, String project, String faq, String keywords)
        throws ProjectNotFoundException, FaqNotFoundException
    {
        // Get list of keywords, minus extraneous characters.
        Collection kwList = Util.getKeywordList(keywords);

        // Get list of projects to be searched.
        ArrayList projList = new ArrayList();
        if (project.equals("*")) {
            projList.addAll(fuser.getProjects());
        } else {
            try {
                int projId = Integer.parseInt(project);
                projList.add(fuser.getProject(projId));
            } catch (NumberFormatException nfe) {
                throw new ProjectNotFoundException
                    ("Bad Project id \"" + project + "\"", nfe);
            }
        }

        ArrayList matches = new ArrayList();
        Iterator projIt = projList.iterator();
        while (projIt.hasNext()) {
            Project p = (Project)projIt.next();

            ArrayList faqList = new ArrayList();
            if (faq.equals("*")) {
                faqList.addAll(p.getFaqs());
            } else {
                try {
                    int faqId = Integer.parseInt(faq);
                    faqList.add(p.getFaq(faqId));
                } catch (NumberFormatException nfe) {
                    throw new FaqNotFoundException
                        ("Bad Faq id \"" + faq + "\"", nfe);
                }
            }

            Iterator faqIt = faqList.iterator();
            while (faqIt.hasNext()) {
                Collection c = searchFaq((Faq)faqIt.next(), kwList);
                matches.addAll(c);
            }
        }
        return matches;
    }

    /**
     * @return the handle to the <code>Database</code> object.
     */
    public Database getDb() {
        return this.db;
    }
}
