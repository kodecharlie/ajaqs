/**
 * GetTag.java
 */

package com.beardediris.ajaqs.templates;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to templatize JSPs.  Specifically, the get-tag
 * is used to retrieve either another JSP or HTML content that resides
 * in the current page's context.  The result is evaluated and then
 * inserted into the current JSP, according to the template layout
 * policy.  The syntax used for this tag is:
 * <pre>
 *   &lt;tmplt:get name="title"/&gt;
 * </pre>
 * <tt>name</tt> identifies an attribute in the page content that
 * holds another JSP or HTML content.</p>
 * <p>See the file <tt>templates.tld</tt> for configuration details.</p>
 *
 * @see PutTag
 * @see InsertTag
 */
public class GetTag extends TagSupport
    implements Constants
{
    private static final String TAGNAME = "get";
    private static final Logger logger =
        Logger.getLogger(GetTag.class.getName());

    private String m_name;

    public void init() {
        m_name = null;
    }

    public GetTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        Stack stack = (Stack)pageContext.getAttribute
            (TEMPLATE_STACK, PageContext.REQUEST_SCOPE);
        if (null == stack) {
            throw new JspException
                ("GetTag could not find stack");
        }

        Hashtable params = (Hashtable)stack.peek();
        if (null == params) {
            throw new JspException
                ("GetTag could not find Hashtable");
        }

        PageParameter param = (PageParameter)params.get(m_name);
        if (null != param) {
            String content = param.getContent();
            if (param.isDirect()) {
                try {
                    pageContext.getOut().print(content);
                } catch (IOException ioe) {
                    throw (JspException)new JspException
                        ("Could not emit content directly").initCause(ioe);
                }
            } else {
                try {
                    pageContext.getOut().flush();
                    pageContext.include(content);
                } catch (IOException ioe) {
                    throw (JspException)new JspException
                        ("Could not include content").initCause(ioe);
                } catch (ServletException se) { 
                    throw (JspException)new JspException
                        ("Could not include content").initCause(se);
                }
            }
        }
        return SKIP_BODY;
    }

    public void release() {
        super.release();
        init();
    }

    public void setName(String name) {
        m_name = name;
    }
    public String getName() {
        return m_name;
    }
}
