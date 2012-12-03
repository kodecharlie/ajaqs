/**
 * EmphasizeTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import com.beardediris.ajaqs.util.Util;

/**
 * <p>This tag emphasizes keywords found in a body of text.  The syntax
 * used for this tag is:
 * <pre>
 *   &lt;ajq:emphasize cssClass="searchword"
 *     keywords="${param.keywords}" text="${r.match}"/&gt;
 * </pre>
 * The resulting text in this example is <tt>r.match</tt>, but
 * with certain substrings highlighted in the following manner.  First,
 * we form a list of keywords from the <tt>keywords</tt> attribute;
 * all whitespaces and punctuation are used to delimit keywords in the
 * string specified in that attribute.  Then for each keyword found in
 * the text specified in the <tt>text</tt> attribute, we emit the
 * following XHTML:
 * <pre>
 *   &lt;span class="searchword"&gt;keyword&lt;span&gt;
 * </pre>
 * The <tt>cssClass</tt> attribute identifies the style-class
 * used to emphasize each keyword.  All surrounding text is rendered
 * as usual.  Note that using this tag will only work if your JSPs
 * define the relevant style-class.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class EmphasizeTag extends TagSupport
{
    private static final String TAGNAME = "emphasize";
    private static final Logger logger =
        Logger.getLogger(EmphasizeTag.class.getName());

    private String m_cssClass;
    private String m_keywords;
    private String m_text;

    private void init() {
        m_cssClass = null;
        m_keywords = null;
        m_text = null;
    }

    public EmphasizeTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        // The "class" we read here is the name of a class from our
        // cascading stylesheets.
        String cssClass = (String)TagHelper.eval
            ("cssclass", m_cssClass, String.class, this, pageContext);
        logger.info("cssclass=" + cssClass);
        if (null == cssClass) {
            throw new NullAttributeException("cssClass", TAGNAME);
        }

        String keywords = (String)TagHelper.eval
            ("keywords", m_keywords, String.class, this, pageContext);
        logger.info("keywords=" + keywords);
        if (null == keywords) {
            throw new NullAttributeException("keywords", TAGNAME);
        }

        String text = (String)TagHelper.eval
            ("text", m_text, String.class, this, pageContext);
        logger.info("text=" + text);
        if (null == text) {
            throw new NullAttributeException("text", TAGNAME);
        }

        // Form the first part of the highlighting markup.
        StringBuffer replace = new StringBuffer();
        replace.append("<span class=\"");
        replace.append(cssClass);
        replace.append("\">");

        // Form regular expression used to search the text for keywords.
        Collection kwColn = Util.getKeywordList(keywords);
        String regex = Util.getRegex(kwColn);

        StringBuffer sb = new StringBuffer();
        int prefLen = replace.length();
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(text);
        while (mat.find()) {
            replace.setLength(prefLen);
            replace.append(mat.group());
            replace.append("</span>");
            mat.appendReplacement(sb, replace.toString());
        }
        mat.appendTail(sb);

        JspWriter writer = pageContext.getOut();
        try {
            writer.print(sb.toString());
        } catch (IOException ioe) {
            throw (JspTagException)new JspTagException
                ("Could not output emphasized text").initCause(ioe);
        }

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getCssClass() {
        return m_cssClass;
    }
    public void setCssClass(String cssClass) {
        m_cssClass = cssClass;
    }

    public String getKeywords() {
        return m_keywords;
    }
    public void setKeywords(String keywords) {
        m_keywords = keywords;
    }

    public String getText() {
        return m_text;
    }
    public void setText(String text) {
        m_text = text;
    }
}
