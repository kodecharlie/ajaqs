/**
 * GetOrSetTag.java
 */

package com.beardediris.ajaqs.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.PageContext;
import com.beardediris.ajaqs.util.Util;
import org.apache.log4j.Logger;

/**
 * <p>This class is sub-classed by tags that either get or set
 * some global variable (ie, attribute) found in either the request,
 * page, session, or application context.  The method
 * <tt>evalExpressions()</tt> should be called before tags
 * process other attributes.  The result is that protected member
 * variable <tt>scope</tt> should specify the context where
 * the relevant attribute is found; the variable <tt>var</tt>
 * will identify the name of the attribute, if it is a <tt>String</tt>,
 * and if it is not, then the variable <tt>varObj</tt> will
 * contain an <tt>Object</tt> which can then be directly
 * manipulated via reflection.  The latter case is a hack to deal
 * with the <em>getProperty</em> tag.</p>
 *
 * @see GetPropertyTag
 * @see SetFaqTag
 * @see SetProjectTag
 * @see SetQuestionTag
 * @see SetUserTag
 */
public class GetOrSetTag extends TagSupport
{
    private static final Logger logger =
        Logger.getLogger(GetOrSetTag.class.getName());

    private String m_var;
    private String m_scope;

    protected String var;
    protected Object varObj;
    protected int scope;

    private void init() {
        m_var = null;
        var = null;
        varObj = null;
        m_scope = null;
        scope = PageContext.PAGE_SCOPE;
    }

    protected void evalExpressions(String tagName)
        throws JspException
    {
        Object obj = TagHelper.eval
            ("var", m_var, Object.class, this, pageContext);
        if (obj instanceof String) {
            var = (String)obj;
            logger.info("var=" + var);
            if (null == var || var.length() <= 0) {
                throw new NullAttributeException("var", tagName);
            }
        } else {
            varObj = obj;
            logger.info("varObj=" + varObj);
            if (null == varObj) {
                throw new NullAttributeException("var", tagName);
            }
        }

        if (null != m_scope && m_scope.length() > 0) {
            String scope = (String)TagHelper.eval
                ("scope", m_scope, String.class, this, pageContext);
            logger.info("scope" + "=" + scope);
            if (null == scope || scope.length() <= 0) {
                throw new NullAttributeException("scope", tagName);
            }
            this.scope = Util.getScope(tagName, scope, "scope");
        }
    }

    public GetOrSetTag() {
        init();
    }

    public void release() {
        super.release();
        init();
    }

    public String getVar() {
        return m_var;
    }
    public void setVar(String var) {
        m_var = var;
    }

    public String getScope() {
        return m_scope;
    }
    public void setScope(String scope) {
        m_scope = scope;
    }
}
