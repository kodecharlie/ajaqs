/**
 * IfContainsTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.Collection;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.db.Role;

/**
 * <p>This tag determines whether some collection contains the
 * specified item.  The syntax used for this tag is:
 * <pre>
 *   &lt;ajq:ifContains collection="${user.projects}"
 *     item="${project}"/&gt;
 * </pre>
 * </p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class IfContainsTag extends ConditionalTagSupport
{
    private static final String TAGNAME = "ifContains";
    private static final Logger logger =
        Logger.getLogger(IfContainsTag.class.getName());

    private Collection m_collection;
    private Object m_item;

    private void init() {
        m_collection = null;
        m_item = null;
    }

    public IfContainsTag() {
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
        if (null == m_collection) {
            throw new NullAttributeException("collection", TAGNAME);
        }

        if (null == m_item) {
            throw new NullAttributeException("item", TAGNAME);
        }

        // Immediately return false if the collection is empty.
        if (m_collection.size() <= 0)
            return false;

        logger.info("item=" + m_item);
        return (m_collection.contains(m_item));
    }

    public Collection getCollection() {
        return m_collection;
    }
    public void setCollection(Collection collection) {
        m_collection = collection;
    }

    public Object getItem() {
        return m_item;
    }
    public void setItem(Object item) {
        m_item = item;
    }
}
