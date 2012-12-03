/**
 * SetQuestionTag.java
 */

package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>Question</code>, and
 * associate it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setQuestion question="${param.question}" faqid="faq"
 *     scope="page" var="question"/&gt;
 * </pre>
 * The attribute <tt>faqid</tt> identifies a <code>Faq</code>
 * that should exist in the given scope.  If a <code>Question</code> with
 * the primary key of <tt>question</tt> (here, specified in a URL parameter
 * as <tt>param.question</tt>) exists in the project, then we associate
 * that <code>Question</code> with the given <tt>var</tt>, in the named
 * scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SetQuestionTag extends GetOrSetTag
{
    private static final String TAGNAME = "setQuestion";
    private static final Logger logger =
        Logger.getLogger(SetQuestionTag.class.getName());

    private String m_question;
    private String m_faqid;

    private void init() {
        m_question = null;
        m_faqid = null;
    }

    public SetQuestionTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        Integer questionId = (Integer)TagHelper.eval
            ("question", m_question, Integer.class, this, pageContext);
        logger.info("questionId=" + questionId);
        if (null == questionId) {
            throw new NullAttributeException("question", TAGNAME);
        }

        String faqid = (String)TagHelper.eval
            ("faqid", m_faqid, String.class, this, pageContext);
        logger.info("faqid=" + faqid);
        if (null == faqid || faqid.length() <= 0) {
            throw new NullAttributeException("faqid", TAGNAME);
        }

        // Get the Faq, which will be used to get the Question.
        Faq faq = (Faq)pageContext.getAttribute(faqid, scope);
        if (null == faq) {
            throw (JspTagException)new JspTagException
                ("Faq with id \"" + faqid + "\"not found");
        }

        Question question = null;
        try {
            question = faq.getQuestion(questionId.intValue());
            logger.info("found question=" + question.getId());
        } catch (QuestionNotFoundException pnfe) {
            throw (JspTagException)new JspTagException
                ("Could not find question \"" + questionId
                 + "\"").initCause(pnfe);
        }
        if (null == question) {
            throw new JspTagException
                ("Could not find question \"" + questionId + "\"");
        }

        // If an attribute already exists for the given var, we
        // overwrite it.
        pageContext.setAttribute(var, question, scope);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getQuestion() {
        return m_question;
    }
    public void setQuestion(String question) {
        this.m_question = question;
    }

    public String getFaqid() {
        return m_faqid;
    }
    public void setFaqid(String faqid) {
        this.m_faqid = faqid;
    }
}
