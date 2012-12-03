/**
 * GetConfigTag.java
 */

package com.beardediris.ajaqs.taglib;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to retrieve either JSTL configuration parameters
 * or application-specific configuration properties.  The syntax used for
 * this tag is:
 * <pre>
 *   &lt;ajq:getConfig param="locale"/&gt;
 * </pre>
 * JSTL parameter names, as above, can be submitted in abbreviated form.
 * The accepted parameter names and corresponding abbreviations are
 * shown in the following table:<br><br>
 * <table cellpadding="3" cellspacing="3" border="1">
 *   <tr align="left">
 *     <th>Parameter name</th>
 *     <th>Abbreviation</th>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.fallbackLocale</td>
 *     <td>fallbackLocale</td>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.locale</td>
 *     <td>locale</td>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.localizationContext</td>
 *     <td>localizationContext</td>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.timeZone</td>
 *     <td>timeZone</td>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.dataSource</td>
 *     <td>dataSource</td>
 *   </tr>
 *   <tr align="left">
 *     <td>javax.servlet.jsp.jstl.fmt.maxRows</td>
 *     <td>maxRows</td>
 *   </tr>
 * </table>
 * </p>
 * <p>See <tt>ajaqs.properties</tt> for a list of accepted
 * application-specific configuration properties.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details
 * for this tag.</p>
 *
 * @see javax.servlet.jsp.jstl.core.Config
 */
public class GetConfigTag extends TagSupport
{
    private static final String TAGNAME = "getConfig";
    private static final Logger logger =
        Logger.getLogger(GetConfigTag.class.getName());

    private static final String[][] configVars = {
        {"fallbackLocale", Config.FMT_FALLBACK_LOCALE},
        {"locale", Config.FMT_LOCALE},
        {"localizationContext", Config.FMT_LOCALIZATION_CONTEXT},
        {"timeZone", Config.FMT_TIME_ZONE},
        {"dataSource", Config.SQL_DATA_SOURCE},
        {"maxRows", Config.SQL_MAX_ROWS}
    };

    /**
     * The abbrev HashMap is read-only.  Hence, we are not
     * concerned about synchronization.
     */
    private static HashMap abbrev = null;
    static {
        if (null == abbrev) {
            abbrev = new HashMap();
            for (int k = 0; k < configVars.length; k++) {
                abbrev.put(configVars[k][0], configVars[k][1]);
            }
        }
    }

    private String m_param;

    private void init() {
        m_param = null;
    }

    public GetConfigTag() {
        init();
    }

    public int doStartTag()
        throws JspException
    {
        String param = (String)TagHelper.eval
            ("param", m_param, String.class, this, pageContext);
        logger.info("param=" + param);
        if (null == param || param.length() <= 0) {
            throw new NullAttributeException("param", TAGNAME);
        }

        // Check if given parameter is an abbreviation.
        if (abbrev.containsKey(param)) {
            param = (String)abbrev.get(param);
            logger.info("param[unabbrev]=" + param);
        }

        // Check if specified parameter is in the JSTL configuration.
        Object obj = Config.get(pageContext, param, PageContext.PAGE_SCOPE);
        if (null == obj) {
            // Configuration parameter not found in JSTL, so try looking
            // in the session for end-user configuration.
            HttpSession session = pageContext.getSession();
            obj = session.getAttribute(param);
            if (null == obj) {
                throw new JspTagException
                    ("No object found for \"" + param + "\"");
            }
        }

        JspWriter writer = pageContext.getOut();
        try {
            writer.print(obj.toString());
        } catch (IOException ioe) {
            throw (JspTagException)new JspTagException
                ("Could not output configuration parameter: \""
                 + param + "\"").initCause(ioe);
        }

        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getParam() {
        return m_param;
    }
    public void setParam(String param) {
        m_param = param;
    }
}
