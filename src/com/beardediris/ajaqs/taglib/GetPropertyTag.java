/**
 * GetPropertyTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.util.Util;

/**
 * <p>This tag is used to retrieve the value of some property
 * associated with an <tt>Object</tt>.  The syntax used for
 * the tag is:
 * <pre>
 *   &lt;ajq:getProperty property="size" var="results" scope="page"/&gt;
 * </pre>
 * There are two cases that can occur when this tag is called:
 * <ul>
 *   <li>If the attribute <tt>var</tt> specifies an <tt>Object</tt> in the
 *   current <tt>pageContext</tt>, then the <tt>property</tt> attribute
 *   is assumed to be associated with that object.  In that case, we
 *   return the value of either <tt>Object.getProperty()</tt> or
 *   <tt>Object.property()</tt>.  If neither method exists in
 *   <tt>Object</tt>, an exception is returned.</li>
 *   <li>If the attribute <tt>var</tt> is a <tt>String</tt>, it is
 *   assumed to reference a global object in the specified scope.
 *   In that case, the object is retrieved from the relevant
 *   context, and the value of the property is returned.  As above,
 *   we assume existence of either <tt>Object.getProperty()</tt> or
 *   <tt>Object.property()</tt>.</li>
 * </ul>
 * </p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class GetPropertyTag extends GetOrSetTag
{
    private static final String TAGNAME = "getProperty";
    private static final Logger logger =
        Logger.getLogger(GetPropertyTag.class.getName());

    private String m_property;

    private void init() {
        m_property = null;
    }

    public GetPropertyTag() {
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        String property = (String)TagHelper.eval
            ("property", m_property, String.class, this, pageContext);
        logger.info("property=" + property);
        if (null == property || property.length() <= 0) {
            throw new NullAttributeException("property", TAGNAME);
        }

        Object obj = null;
        if (null != var) {
            obj = pageContext.getAttribute(var, scope);
        } else {
            obj = varObj;
        }
        if (null == obj) {
            throw new JspTagException("No object found.");
        }

        Object propertyValue = Util.getProperty(obj, property);
        logger.info("propertyValue=" + propertyValue);

        // Only output something if the property is not empty.
        if (null != propertyValue) {
            JspWriter writer = pageContext.getOut();
            try {
                logger.info("propertyValue=" + propertyValue);
                if (propertyValue instanceof String) {
                    writer.print((String)propertyValue);
                } else {
                    writer.print(propertyValue.toString());
                }
            } catch (IOException ioe) {
                throw (JspTagException)new JspException
                    ("Could not get property value").initCause(ioe);
            }
        }

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getProperty() {
        return m_property;
    }
    public void setProperty(String property) {
        m_property = property;
    }
}
