/**
 * InsertTag.java
 */

package com.beardediris.ajaqs.templates;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>This tag is used to insert and populate a template within
 * the current JSP.</p>
 * <p>See the file <tt>templates.tld</tt> for configuration details.</p>
 *
 * @see GetTag
 * @see PutTag
 */
public class InsertTag extends TagSupport
    implements Constants
{
    private String m_template;
    private Stack m_stack;

    private void init() {
        m_template = null;
        m_stack = null;
    }

    public InsertTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        m_stack = getStack();
        m_stack.push(new Hashtable());
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        try {
            pageContext.include(m_template);
        } catch (IOException ioe) {
            throw (JspException)new JspException
                ("Could not end InsertTag").initCause(ioe);
        } catch (ServletException se) {
            throw (JspException)new JspException
                ("Could not end InsertTag").initCause(se);
        }

        m_stack.pop();
        return EVAL_PAGE;
    }

    public void release() {
        super.release();
        init();
    }

    public Stack getStack()
    {
        Stack stack = (Stack)pageContext.getAttribute
            (TEMPLATE_STACK, PageContext.REQUEST_SCOPE);
        if (null == stack) {
            stack = new Stack();
            pageContext.setAttribute
                (TEMPLATE_STACK, stack, PageContext.REQUEST_SCOPE);
        }
        return stack;
    }

    public void setTemplate(String template) {
        m_template = template;
    }
    public String getTemplate() {
        return m_template;
    }
}
