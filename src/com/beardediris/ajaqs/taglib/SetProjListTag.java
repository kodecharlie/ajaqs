/**
 * SetProjListTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Role;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to find and store a list of projects.
 * The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setProjList logon="${pageContext.request.remoteUser}"
 *     var="projList" scope="page" all="false"/&gt;
 * </pre>
 * Using the above tag will create an attribute named <tt>projList</tt>
 * in the page context that is associated with a <code>Collection</code>
 * of projects.  If the attribute already exists, it is overwritten.
 * <tt>logon</tt> is the name of the user who initiated this request;
 * it is an optional attribute; if specified, the operation is disallowed
 * if that user does not have administrative priveleges; otherwise,
 * if the optional argument <tt>all</tt> is specified and is true,
 * then <tt>projList</tt> is set to all projects on record; if <tt>all</tt>
 * is false, then only those projects on record for the user
 * designated by <tt>logon</tt> are retrieved.  Note that the default
 * value of <tt>all</tt> is true.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 *
 * @see com.beardediris.ajaqs.oql.QueryDB
 */
public class SetProjListTag extends GetOrSetTag
{
    private static final String TAGNAME = "setProjList";
    private static final Logger logger =
        Logger.getLogger(SetProjListTag.class.getName());

    private String m_logon;
    private String m_all;

    private void init() {
        m_logon = null;
        m_all = null;
    }

    /**
     * Throw an exception if the user with the specified logon does not
     * have admin privileges.  Otherwise, return the FaqUser.
     */
    private FaqUser validateUser(String logon)
        throws JspException
    {
        FaqUser curUser = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            curUser = (FaqUser)query.getFaqUser(logon, false);
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("User with logon \"" + logon + "\"not found")
                .initCause(dnfe);
        } catch (UserNotFoundException unfe) {
            throw (JspTagException)new JspTagException
                ("User with logon \"" + logon + "\"not found")
                .initCause(unfe);
        }

        // Make certain this user has the admin role.
        boolean hasAdminRole = false;
        Iterator it = curUser.getRoles().iterator();
        while (it.hasNext()) {
            Role r = (Role)it.next();
            if (r.getRoleName().equals(Constants.ADMIN)) {
                hasAdminRole = true;
                break;
            }
        }
        if (! hasAdminRole) {
            throw new JspException
                ("User with logon \"" + curUser.getLogon()
                 + "\" does not have administrative priveleges");
        }

        return curUser;
    }

    public SetProjListTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        // Prohibit the query if the user is not an admin,
        // but only if the logon attribute is specified.
        FaqUser fuser = null;
        if (null != m_logon) {
            String logon = (String)TagHelper.eval
                ("logon", m_logon, String.class, this, pageContext);
            logger.info("logon=" + logon);
            fuser = validateUser(logon);
        }

        // By default, we will retrieve all projects.
        boolean all = true;
        if (null != m_all) {
            // This is a sanity check.  The ALL flag should be used
            // only if a specific user is making the query.
            if (null == fuser) {
                throw new JspTagException
                    ("Can only use \"all\" attribute if \"logon\" "
                     + "is specified");
            }

            Boolean b = (Boolean)TagHelper.eval
                ("all", m_all, Boolean.class, this, pageContext);
            all = b.booleanValue();
        }

        // Get all existing users.
        Collection projList = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            if ((null != fuser && all) || (null == fuser)) {
                projList = query.getAllProjects();
            } else {
                projList = fuser.getProjects();
            }
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of projects").initCause(dnfe);
        } catch (ProjectNotFoundException unfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of projects").initCause(unfe);
        }
        if (null == projList) {
            throw new JspTagException
                ("Could not retrieve list of projects");
        }

        // If attribute already exists, we overwrite it.
        pageContext.setAttribute(var, projList, scope);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getLogon() {
        return m_logon;
    }
    public void setLogon(String logon) {
        m_logon = logon;
    }
    public void setAll(String all) {
        m_all = all;
    }
}
