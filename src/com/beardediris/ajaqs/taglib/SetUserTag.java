/**
 * SetUserTag.java
 */

package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.DuplicateIdException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import com.beardediris.ajaqs.session.Constants;
import com.beardediris.ajaqs.session.RecordLoginTime;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>FaqUser</code>, and
 * associate it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setUser logon="${pageContext.request.remoteUser}"
 *     scope="page" var="user"/&gt;
 * </pre>
 * The backend is queried for a <code>FaqUser</code> with the
 * specified <tt>logon</tt>.  If that user is found, then the
 * <code>FaqUser</code> object is associated with the given <tt>var</tt>
 * in the named scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 *
 * @see com.beardediris.ajaqs.session.LoadUserConfig
 */
public class SetUserTag extends GetOrSetTag
    implements Constants
{
    private static final String TAGNAME = "setUser";
    private static final Logger logger =
        Logger.getLogger(SetUserTag.class.getName());

    private String m_logon;

    private void init() {
        m_logon = null;
    }

    public SetUserTag() {
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

        FaqUser fuser = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            fuser = (FaqUser)query.getFaqUser(logon, false);
            logger.info("found user=" + fuser.getLogon());
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("Could not find user \"" + logon
                 + "\"").initCause(dnfe);
        } catch (UserNotFoundException unfe) {
            throw (JspTagException)new JspTagException
                ("Could not find user \"" + logon
                 + "\"").initCause(unfe);
        }
        if (null == fuser) {
            throw new JspTagException
                ("Could not find user \"" + logon + "\"");
        }

        // If attribute already exists, we overwrite it.
        pageContext.setAttribute(var, fuser, scope);

        // Save name of user so that we can record the login time
        // for the user when the session terminates. This is somewhat
        // hacky, as we assume that any session initiated by the
        // end-user will lead here.
        RecordLoginTime rlt = (RecordLoginTime)pageContext.getAttribute
            (RECORD_LOGIN_TIME, PageContext.SESSION_SCOPE);
        if (null == rlt) {
            rlt = new RecordLoginTime(logon);
            pageContext.getSession().setAttribute(RECORD_LOGIN_TIME, rlt);
            logger.info("Setting attribute: " + RECORD_LOGIN_TIME);
            logger.info("RecordLoginTime: "
              + (new java.util.Date(rlt.getCreationTime())).toString());
        }

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
