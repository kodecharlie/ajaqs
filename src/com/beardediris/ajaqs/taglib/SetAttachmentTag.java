/**
 * SetAttachmentTag.java
 */

package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.Attachment;
import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.ex.AttachmentNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>Attachment</code>, and
 * associate it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setAttachment attachment="${param.attachment}"
 *     answerid="answer" scope="page" var="attachment"/&gt;
 * </pre>
 * The attribute <tt>answerid</tt> identifies a <code>Answer</code>
 * that should exist in the given scope.  If an <code>Attachment</code> with
 * the primary key of <tt>attachment</tt> (here, specified in a URL parameter
 * as <tt>param.attachment</tt>) exists for the Answer, then we associate
 * that <code>Attachment</code> with the given <tt>var</tt>, in the named
 * scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SetAttachmentTag extends GetOrSetTag
{
    private static final String TAGNAME = "setAttachment";
    private static final Logger logger =
        Logger.getLogger(SetAttachmentTag.class.getName());

    private String m_attachment;
    private String m_answerid;

    private void init() {
        m_attachment = null;
        m_answerid = null;
    }

    public SetAttachmentTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        Integer attachmentId = (Integer)TagHelper.eval
            ("attachment", m_attachment, Integer.class, this, pageContext);
        logger.info("attachmentId=" + attachmentId);
        if (null == attachmentId) {
            throw new NullAttributeException("attachment", TAGNAME);
        }

        String answerid = (String)TagHelper.eval
            ("answerid", m_answerid, String.class, this, pageContext);
        logger.info("answerid=" + answerid);
        if (null == answerid || answerid.length() <= 0) {
            throw new NullAttributeException("answerid", TAGNAME);
        }

        // Get the Answer, which will be used to get the Attachment.
        Answer answer = (Answer)pageContext.getAttribute
            (answerid, scope);
        if (null == answer) {
            throw (JspTagException)new JspTagException
                ("Answer with id \"" + answerid + "\"not found");
        }

        Attachment attachment = null;
        try {
            attachment = answer.getAttachment(attachmentId.intValue());
            logger.info("found attachment=" + attachment.getId());
        } catch (AttachmentNotFoundException pnfe) {
            throw (JspTagException)new JspTagException
                ("Could not find attachment \"" + attachmentId
                 + "\"").initCause(pnfe);
        }
        if (null == attachment) {
            throw new JspTagException
                ("Could not find attachment \"" + attachmentId + "\"");
        }

        // If an attribute already exists for the given var, we
        // overwrite it.
        pageContext.setAttribute(var, attachment, scope);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getAttachment() {
        return m_attachment;
    }
    public void setAttachment(String attachment) {
        m_attachment = attachment;
    }

    public String getAnswerid() {
        return m_answerid;
    }
    public void setAnswerid(String answerid) {
        m_answerid = answerid;
    }
}
