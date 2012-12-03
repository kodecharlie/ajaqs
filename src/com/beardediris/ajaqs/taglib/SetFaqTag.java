/**
 * SetFaqTag.java
 */

package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>Faq</code>, and associate
 * it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setFaq faq="${param.faq}" projectid="project"
       scope="page" var="faq"/&gt;
 * </pre>
 * The attribute <tt>projectid</tt> identifies a <code>Project</code>
 * that should exist in the given scope.  If a <code>Faq</code> with
 * the primary key of <tt>faq</tt> (here, specified in a URL parameter
 * as <tt>param.faq</tt>) exists in the project, then we associate
 * that <code>Faq</code> with the given <tt>var</tt>, in the named
 * scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SetFaqTag extends GetOrSetTag
{
    private static final String TAGNAME = "setFaq";
    private static final Logger logger =
        Logger.getLogger(SetFaqTag.class.getName());

    private String m_faq;
    private String m_projectid;

    private void init() {
        m_faq = null;
        m_projectid = null;
    }

    public SetFaqTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        Integer faqId = (Integer)TagHelper.eval
            ("faq", m_faq, Integer.class, this, pageContext);
        logger.info("faqId=" + faqId);
        if (null == faqId) {
            throw new NullAttributeException("faq", TAGNAME);
        }

        String projectid = (String)TagHelper.eval
            ("projectid", m_projectid, String.class, this, pageContext);
        logger.info("projectid=" + projectid);
        if (null == projectid || projectid.length() <= 0) {
            throw new NullAttributeException("projectid", TAGNAME);
        }

        // Get the Project, which will be used to get the Faq.
        Project project = (Project)pageContext.getAttribute(projectid, scope);
        if (null == project) {
            throw (JspTagException)new JspTagException
                ("Project with id \"" + projectid + "\" not found");
        }

        Faq faq = null;
        try {
            faq = project.getFaq(faqId.intValue());
            logger.info("found faq=" + faq.getName());
        } catch (FaqNotFoundException pnfe) {
            throw (JspTagException)new JspTagException
                ("Could not find faq \"" + faqId
                 + "\"").initCause(pnfe);
        }
        if (null == faq) {
            throw new JspTagException
                ("Could not find faq \"" + faqId + "\"");
        }

        // If an attribute already exists for the given var, we
        // overwrite it.
        pageContext.setAttribute(var, faq, scope);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getFaq() {
        return m_faq;
    }
    public void setFaq(String faq) {
        this.m_faq = faq;
    }

    public String getProjectid() {
        return m_projectid;
    }
    public void setProjectid(String projectid) {
        this.m_projectid = projectid;
    }
}
