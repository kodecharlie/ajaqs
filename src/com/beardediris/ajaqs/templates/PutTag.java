/**
 * PutTag.java
 */

package com.beardediris.ajaqs.templates;

import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.taglib.TagHelper;
import com.beardediris.ajaqs.taglib.NullAttributeException;

/**
 * <p>This tag is used to insert JSP pages or HTML content into
 * the current page context for later retrieval by the get-tag.
 * The syntax used for this tag is:
 * <pre>
 *   &lt;tmplt:put name="title" content="Hello World" direct="true"/&gt;
 * </pre>
 * <tt>name</tt> identifies the attribute in the page context
 * that will hold the JSP or HTML content.  <tt>content</tt>
 * identifies the JSP or HTML content.  <tt>direct</tt> is optional
 * and indicates whether <tt>content</tt> is HTML or not; the
 * default is <tt>false</tt>.</p>
 * <p>See the file <tt>templates.tld</tt> for configuration details.</p>
 *
 * @see GetTag
 * @see InsertTag
 */
public class PutTag extends TagSupport
{
    private static final String TAGNAME = "put";
    private static final Logger logger =
        Logger.getLogger(PutTag.class.getName());

    private String m_name;
    private String m_content;
    private String m_direct;

    private void init() {
        m_name = null;
        m_content = null;
        m_direct = "false";
    }

    private TagSupport getAncestor(String className) 
        throws JspException
    {
        Class klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw (JspException)new JspException
                ("Could not find ancestor for \"" + className + "\"")
                .initCause(ex);
        }
        return (TagSupport)findAncestorWithClass(this, klass);
    }

    public PutTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        InsertTag parent = (InsertTag)getAncestor
            ("com.beardediris.ajaqs.templates.InsertTag");
        if (null == parent) {
            throw new JspException
                ("PutTag must be used within InsertTag");
        }

        Stack templateStack = parent.getStack();
        if (null == templateStack) {
            throw new JspException("PutTag: no template stack");
        }

        Hashtable params = (Hashtable)templateStack.peek();
        if (null == params) {
            throw new JspException("PutTag: no hashtable");
        }

        // Evaluate content, as it may be a variable.
        String content = (String)TagHelper.eval
            ("content", m_content, String.class, this, pageContext);
        logger.info("content=" + content);
        if (null == content || content.length() <= 0) {
            throw new NullAttributeException("content", TAGNAME);
        }
        params.put(m_name, new PageParameter(content, m_direct));

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

    public void setContent(String content) {
        m_content = content;
    }
    public String getContent() {
        return m_content;
    }

    public void setDirect(String direct) {
        m_direct = direct;
    }
    public String getDirect() {
        return m_direct;
    }
}
