/**
 * CheckBrowserTag.java
 */

package com.beardediris.ajaqs.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import org.apache.log4j.Logger;

/**
 * <p>This tag checks whether the client browser is Netscape 4.7 or less.
 * For MSIE, we always assume that this is false.  The syntax for the
 * tag is:
 * <pre>
 *   &lt;ajq:checkBrowser agent="${sessionScope.userAgent}"
 *     var="isNetscape47" scope="session"/&gt;
 * </pre>
 * </p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class CheckBrowserTag extends ConditionalTagSupport
{
    private static final String TAGNAME = "checkBrowser";
    private static final Logger logger =
        Logger.getLogger(CheckBrowserTag.class.getName());

    private static final String MOZILLA = "mozilla";
    private static final String MSIE = "MSIE";

    private String m_agent;

    private void init() {
        m_agent = null;
    }

    public CheckBrowserTag() {
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
        String userAgent = (String)TagHelper.eval
            ("agent", m_agent, String.class, this, pageContext);
        logger.info("userAgent=" + userAgent);
        if (null == userAgent || userAgent.length() <= 0) {
            throw new NullAttributeException("agent", TAGNAME);
        }

        // We blindly assume version 5.0 or greater for MSIE.
        int msie = userAgent.indexOf(MSIE);
        if (msie >= 0) {
            return false;
        }

        // Check for Netscape 4.x or less.
        boolean haveNetscape47 = false;
        int idx = userAgent.toLowerCase().indexOf(MOZILLA);
        if (idx >= 0) {
            // Get the version and check if it is <= 4.
            // We expect to parse a string like "Mozilla/4.7".
            // Only look at the first digit to see what the
            // major version number is.
            idx += MOZILLA.length() + 1;
            try {
                int version = Integer.parseInt
                    (userAgent.substring(idx, idx+1));
                if (version <= 4) {
                    haveNetscape47 = true;
                }
            } catch (IndexOutOfBoundsException ioobe) {
                throw (JspTagException)new JspTagException
                    ("Could not parse version number").initCause(ioobe);
            } catch (NumberFormatException nfe) {
                throw (JspTagException)new JspTagException
                    ("Could not parse version number").initCause(nfe);
            }
        }
        return (haveNetscape47);
    }

    public String getAgent() {
        return m_agent;
    }
    public void setAgent(String agent) {
        m_agent = agent;
    }
}
