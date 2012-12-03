/**
 * IfVisibleTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.Collection;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Hideable;
import com.beardediris.ajaqs.session.Constants;

/**
 * <p>This tag determines whether some object is visible.
 * The syntax used for this tag is:
 * <pre>
 *   &lt;ajq:ifVisible object="${project}"/&gt;
 * </pre>
 * The attribute <tt>object</tt> identifies the object to be tested.
 * For example, if <tt>project</tt> here has an ACTIVE state, then
 * it is visible.  Alternatively, if <tt>project</tt> has an INACTIVE
 * state, but the end-user configuration option
 * <pre>
 *   com.beardediris.ajaqs.session.showInactiveProjects
 * </pre>
 * is <tt>true</tt>, then again, the <tt>project</tt> is visible.
 * Otherwise, the <tt>project</tt> is not visible.</p>
 *
 * <p>Objects that can be specified in the <tt>object</tt> attribute
 * are <tt>FaqUser</tt>s, <tt>Project</tt>, and <tt>Faq</tt>s.
 * The relevant end-user configuration options for each object
 * are given in the following table:
 * <table>
 *   <tr align="center">
 *     <th>Object</th>
 *     <th>Option</th>
 *   </tr>
 *   <tr align="center">
 *     <td>FaqUser</td>
 *     <td>com.beardediris.ajaqs.session.showInactiveUsers</td>
 *   </tr>
 *   <tr align="center">
 *     <td>Project</td>
 *     <td>com.beardediris.ajaqs.session.showInactiveProjects</td>
 *   </tr>
 *   <tr align="center">
 *     <td>Faq</td>
 *     <td>com.beardediris.ajaqs.session.showInactiveFaqs</td>
 *   </tr>
 * </table>
 * </p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class IfVisibleTag extends ConditionalTagSupport
    implements Constants
{
    private static final String TAGNAME = "ifVisible";
    private static final Logger logger =
        Logger.getLogger(IfVisibleTag.class.getName());

    private String m_object;

    private void init() {
        m_object = null;
    }

    public IfVisibleTag() {
        init();
    }

    public void release()
    {
        super.release();
        init();
    }

    protected boolean condition()
        throws JspTagException
    {
        Object object = (Object)TagHelper.eval
            ("object", m_object, Object.class, this, pageContext);
        logger.info("object=" + object.getClass().getName());
        if (null == object) {
            throw new NullAttributeException("object", TAGNAME);
        }

        boolean visibleFlag = false;
        switch (((Hideable)object).getState()) {

        case Hideable.ACTIVE:
            visibleFlag = true;
            break;

        case Hideable.INACTIVE:
            ServletContext sctx = pageContext.getServletContext();
            Properties props = (Properties)sctx.getAttribute
                (com.beardediris.ajaqs.session.Constants.CONFIG_ATTR);

            String showInactive = null;
            if (object instanceof FaqUser) {
                showInactive = props.getProperty(SHOW_INACTIVE_USERS);
            } else if (object instanceof Project) {
                showInactive = props.getProperty(SHOW_INACTIVE_PROJECTS);
            } else if (object instanceof Faq) {
                showInactive = props.getProperty(SHOW_INACTIVE_FAQS);
            }

            Boolean b = new Boolean(showInactive);
            visibleFlag = b.booleanValue();
            break;

        default:
            throw new JspTagException("Object in undefined state");
        }

        return visibleFlag;
    }

    public String getObject() {
        return m_object;
    }
    public void setObject(String object) {
        m_object = object;
    }
}
