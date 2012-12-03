/**
 * NullAttributeException.java
 */

package com.beardediris.ajaqs.taglib;

import javax.servlet.jsp.JspTagException;

/**
 * <p>This exception is used whenever an attribute required for
 * some tag is found to be empty.</p>
 */
public class NullAttributeException extends JspTagException
{
    /**
     * This constructor just creates a useful error message and returns.
     *
     * @param attribute name of empty or null attribute.
     * @param action name of tag that failed because attribute was empty.
     */
    public NullAttributeException(String attribute, String action)
    {
        super(new StringBuffer().
              append("The attribute \"").append(attribute).
              append("\" was found to be empty in the \"").append(action).
              append("\" action.").toString());
    }
}
