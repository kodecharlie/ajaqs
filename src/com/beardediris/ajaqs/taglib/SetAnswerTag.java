/**
 * SetAnswerTag.java
 */

package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.AnswerNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>Answer</code>, and
 * associate it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setAnswer answer="${param.answer}" questionid="question"
 *     scope="page" var="answer"/&gt;
 * </pre>
 * The attribute <tt>questionid</tt> identifies a <code>Question</code>
 * that should exist in the given scope.  If an <code>Answer</code> with
 * the primary key of <tt>answer</tt> (here, specified in a URL parameter
 * as <tt>param.answer</tt>) exists for the Question, then we associate
 * that <code>Answer</code> with the given <tt>var</tt>, in the named
 * scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SetAnswerTag extends GetOrSetTag
{
    private static final String TAGNAME = "setQuestion";
    private static final Logger logger =
        Logger.getLogger(SetAnswerTag.class.getName());

    private String m_answer;
    private String m_questionid;

    private void init() {
        m_answer = null;
        m_questionid = null;
    }

    public SetAnswerTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // Evaluate attributes in parent class.
        evalExpressions(TAGNAME);

        Integer answerId = (Integer)TagHelper.eval
            ("answer", m_answer, Integer.class, this, pageContext);
        logger.info("answerId=" + answerId);
        if (null == answerId) {
            throw new NullAttributeException("answer", TAGNAME);
        }

        String questionid = (String)TagHelper.eval
            ("questionid", m_questionid, String.class, this, pageContext);
        logger.info("questionid=" + questionid);
        if (null == questionid || questionid.length() <= 0) {
            throw new NullAttributeException("questionid", TAGNAME);
        }

        // Get the Question, which will be used to get the Answer.
        Question question = (Question)pageContext.getAttribute
            (questionid, scope);
        if (null == question) {
            throw (JspTagException)new JspTagException
                ("Question with id \"" + questionid + "\"not found");
        }

        Answer answer = null;
        try {
            answer = question.getAnswer(answerId.intValue());
            logger.info("found answer=" + answer.getId());
        } catch (AnswerNotFoundException pnfe) {
            throw (JspTagException)new JspTagException
                ("Could not find answer \"" + answerId
                 + "\"").initCause(pnfe);
        }
        if (null == answer) {
            throw new JspTagException
                ("Could not find answer \"" + answerId + "\"");
        }

        // If an attribute already exists for the given var, we
        // overwrite it.
        pageContext.setAttribute(var, answer, scope);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getAnswer() {
        return m_answer;
    }
    public void setAnswer(String answer) {
        this.m_answer = answer;
    }

    public String getQuestionid() {
        return m_questionid;
    }
    public void setQuestionid(String questionid) {
        this.m_questionid = questionid;
    }
}
