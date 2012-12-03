/**
 * GetError.java
 */

package com.beardediris.ajaqs.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.session.Constants;

/**
 * <p>This tag is used to retrieve the text portion of an
 * internal exception stored in the request context.  Typically,
 * this tag is called from <tt>error.jsp</tt> when an exception
 * occurs but for which there is otherwise no special error-handling.
 * The syntax used for this tag is:
 * <pre>
 *   &lt;ajq:getError/&gt;
 * </pre>
 * </p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details
 * for this tag.</p>
 *
 * @see com.beardediris.ajaqs.session.Constants
 */
public class GetErrorTag extends TagSupport
    implements Constants
{
    private static final String TAGNAME = "getError";
    private static final Logger logger =
        Logger.getLogger(GetErrorTag.class.getName());

    public GetErrorTag() {
        // nothing
    }

    public int doStartTag()
        throws JspException
    {
        // Retrieve exception.
        Exception ex = (Exception)pageContext.getAttribute
            (INTERNAL_EXCEPTION, PageContext.REQUEST_SCOPE);
        if (null == ex) {
            throw new JspTagException("No exception found");
        }

        JspWriter writer = pageContext.getOut();
        try {
            // Print the exception text.
            writer.print(ex.toString());
            writer.print("<br /><br />");

            // Emit stack trace leading to exception.
            StackTraceElement[] trace = ex.getStackTrace();
            for (int k = 0; k < trace.length; k++) {
                writer.print(trace[k].toString());
                writer.print("<br />");
            }
        } catch (IOException ioe) {
            throw (JspTagException)new JspTagException
                ("Could not write the exception").initCause(ioe);
        }

        return super.doStartTag();
    }

    public void release() {
        super.release();
    }
}
