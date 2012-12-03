/**
 * SearchProjectTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.util.Collection;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.QuestionNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to search questions and answers for keywords
 * submitted by an Ajaqs user.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:searchProject var="results" keywords="${param.keywords}"
 *     userid="user" project="${projSpec}" faq="${faqSpec}"/&gt;
 * </pre>
 * <b>NOTE</b> that we make two crucial assumptions in this tag
 * that concern scope:
 * <ol>
 *   <li>
 *     <tt>userid</tt> identifies the active <code>FaqUser</code>, which
 *     is stored in the <em>page scope</em>.
 *   </li>
 *   <li>
 *     <tt>var</tt> identifies the name of an attribute that will be
 *     stored in the <em>page scope</em>.
 *   </li>
 * </ol>
 * Our search is linear.  That is, after getting the <code>Faq</code>
 * (named by the <tt>faq</tt> attribute) from the <code>Project</code>
 * (named by the <tt>project</tt> attribute), and the <code>Project</code>
 * from the <code>FaqUser</code>, we then iterate through each
 * <code>Question</code> in the FAQ.  A search through a question
 * is considered to yield a positive result in one of two cases:
 * <ol>
 *   <li>
 *     The question itself contains at least one substring that
 *     matches one of the keywords.  In that case, a match consists
 *     of the questions itself, along with the first (if any) answer
 *     associated with that question.  This match is returned to the
 *     JSP.
 *   </li>
 *   <li>
 *     The question does not contain any substring that matches a
 *     keyword.  However, at least one answer does.  The match then
 *     consists of the question and that portion of the matching
 *     answer which surrounds the first substring is equal to one
 *     of the keywords.  This match is returned to the JSP.
 *   </li>
 * </ol>
 * If no matches are found, then <tt>var</tt> will hold an empty
 * <code>Collection</code>.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SearchProjectTag extends TagSupport
{
    private static final String TAGNAME = "searchProject";
    private static final Logger logger =
        Logger.getLogger(SearchProjectTag.class.getName());

    private String m_var;
    private String m_keywords;
    private String m_userid;
    private String m_project;
    private String m_faq;

    private void init() {
        m_var = null;
        m_keywords = null;
        m_userid = null;
        m_project = null;
        m_faq = null;
    }

    public SearchProjectTag() {
        super();
        init();
    }

    public int doStartTag()
        throws JspException
    {
        String var = (String)TagHelper.eval
            ("var", m_var, String.class, this, pageContext);
        logger.info("var=" + var);
        if (null == var || var.length() <= 0) {
            throw new NullAttributeException("var", TAGNAME);
        }

        // Note: we allow for an empty string for keywords.
        String keywords = (String)TagHelper.eval
            ("keywords", m_keywords, String.class, this, pageContext);
        logger.info("keywords=" + keywords);
        if (null == keywords) {
            throw new NullAttributeException("keywords", TAGNAME);
        }

        String userid = (String)TagHelper.eval
            ("userid", m_userid, String.class, this, pageContext);
        logger.info("userid=" + userid);
        if (null == userid || userid.length() <= 0) {
            throw new NullAttributeException("userid", TAGNAME);
        }

        String project = (String)TagHelper.eval
            ("project", m_project, String.class, this, pageContext);
        logger.info("project=" + project);
        if (null == project || project.length() <= 0) {
            throw new NullAttributeException("project", TAGNAME);
        }

        String faq = (String)TagHelper.eval
            ("faq", m_faq, String.class, this, pageContext);
        logger.info("faq=" + faq);
        if (null == faq || faq.length() <= 0) {
            throw new NullAttributeException("faq", TAGNAME);
        }

        // Get the FaqUser. NOTE: we assume page scope.
        FaqUser fuser = (FaqUser)pageContext.getAttribute
            (userid, PageContext.PAGE_SCOPE);
        if (null == fuser) {
            throw new JspTagException
                ("FaqUser with id \"" + userid + "\"not found");
        }

        Collection matches = null;
        try {
            QueryDB query = new QueryDB(pageContext);
            matches = query.findMatches(fuser, project, faq, keywords);
        } catch (DatabaseNotFoundException dnfe) {
            throw (JspTagException)new JspTagException
                ("Could not complete search").initCause(dnfe);
        } catch (ProjectNotFoundException pnfe) {
            throw (JspTagException)new JspTagException
                ("Could not complete search").initCause(pnfe);
        } catch (FaqNotFoundException fnfe) {
            throw (JspTagException)new JspTagException
                ("Could not complete search").initCause(fnfe);
        }
        // NOTE: we assume page scope.
        pageContext.setAttribute(var, matches, PageContext.PAGE_SCOPE);

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getVar() {
        return m_var;
    }
    public void setVar(String var) {
        m_var = var;
    }

    public String getKeywords() {
        return m_keywords;
    }
    public void setKeywords(String keywords) {
        m_keywords = keywords;
    }

    public String getUserid() {
        return m_userid;
    }
    public void setUserid(String userid) {
        m_userid = userid;
    }

    public String getProject() {
        return m_project;
    }
    public void setProject(String project) {
        m_project = project;
    }

    public String getFaq() {
        return m_faq;
    }
    public void setFaq(String faq) {
        m_faq = faq;
    }
}
