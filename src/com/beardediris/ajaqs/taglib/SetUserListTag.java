/**
 * SetUserListTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Role;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to find and store the list of all users,
 * excluding the current user.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setUserList logon="${pageContext.request.remoteUser}"
 *     var="userList" scope="page"/&gt;
 * </pre>
 * Using the above tag will create an attribute named <tt>userList</tt>
 * in the page context that is associated with a <code>Collection</code>
 * of all users, minus the current one.  If the attribute already
 * exists, it is overwritten.  <tt>logon</tt> is the name of the user
 * who initiated this request; the operation is disallowed if that
 * user does not have administrative priveleges.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 *
 * @see com.beardediris.ajaqs.oql.QueryDB
 */
public class SetUserListTag extends GetOrSetTag
{
    private static final String TAGNAME = "setUserList";
    private static final Logger logger =
        Logger.getLogger(SetUserListTag.class.getName());

    private String m_logon;

    private void init() {
        m_logon = null;
    }

    /**
     * Get all users, minus the current one.  This operation
     * is only permitted for those users with the admin role.
     */
    private Collection getAllUsers(FaqUser curUser)
        throws JspTagException
    {
        // Get all existing users.
        Collection all = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            all = query.getAllFaqUsers(true);
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of users").initCause(dnfe);
        } catch (UserNotFoundException unfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of users").initCause(unfe);
        }
        if (null == all) {
            throw new JspTagException
                ("Could not retrieve list of users");
        }

        // Prune the current user from the list of all users.
        ArrayList pruned = new ArrayList();
        Iterator it = all.iterator();
        while (it.hasNext()) {
            FaqUser fuser = (FaqUser)it.next();
            if (! fuser.getLogon().equals(curUser.getLogon())) {
                pruned.add(fuser);
            }
        }
        return pruned;
    }

    public SetUserListTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        String logon = (String)TagHelper.eval
            ("logon", m_logon, String.class, this, pageContext);
        logger.info("logon=" + logon);
        if (null == logon || logon.length() <= 0) {
            throw new NullAttributeException("logon", TAGNAME);
        }

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

        // If attribute already exists, we overwrite it.
        Collection userList = getAllUsers(curUser);
        pageContext.setAttribute(var, userList, scope);

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
        this.m_logon = logon;
    }
}
