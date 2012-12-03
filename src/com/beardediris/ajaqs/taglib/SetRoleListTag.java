/**
 * SetRoleListTag.java
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
import com.beardediris.ajaqs.ex.RoleNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to find and store the list of all roles.
 * The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setRoleList logon="${pageContext.request.remoteUser}"
 *     var="roleList" scope="page"/&gt;
 * </pre>
 * Using the above tag will create an attribute named <tt>roleList</tt>
 * in the page context that is associated with a <code>Collection</code>
 * of all roles.  If the attribute already exists, it is
 * overwritten.  <tt>logon</tt> is the name of the user
 * who initiated this request; it is an optional attribute;
 * if specified, the operation is disallowed if that
 * user does not have administrative priveleges.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 *
 * @see com.beardediris.ajaqs.oql.QueryDB
 */
public class SetRoleListTag extends GetOrSetTag
{
    private static final String TAGNAME = "setRoleList";
    private static final Logger logger =
        Logger.getLogger(SetRoleListTag.class.getName());

    private String m_logon;

    private void init() {
        m_logon = null;
    }

    private void validateUser(String logon)
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
    }

    public SetRoleListTag() {
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
        if (null != m_logon) {
            String logon = (String)TagHelper.eval
                ("logon", m_logon, String.class, this, pageContext);
            logger.info("logon=" + logon);
            validateUser(logon);
        }

        // Get all existing users.
        Collection roleList = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            roleList = query.getAllRoles();
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of roles").initCause(dnfe);
        } catch (RoleNotFoundException unfe) {
            throw (JspTagException)new JspTagException
                ("Could not retrieve list of roles").initCause(unfe);
        }
        if (null == roleList) {
            throw new JspTagException
                ("Could not retrieve list of roles");
        }

        // If attribute already exists, we overwrite it.
        pageContext.setAttribute(var, roleList, scope);

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
