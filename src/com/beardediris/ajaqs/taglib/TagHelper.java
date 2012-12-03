/**
 * TagHelper.java
 */

package com.beardediris.ajaqs.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * <p>This class contains static methods of use to classes that
 * implement tags.</p>
 */
public class TagHelper {
    /**
     * Evaluate the given expression, and return the resulting
     * object.
     *
     * @param attrName is the name of the attribute that contained
     * the expression.
     * @param expr contains the expression to be evaluated.
     * @param theClass is the <tt>Class</tt> defining desired type
     * of result.
     * @param tag is the name of the tag containing the attribute
     * being evaluated.
     * @param pctx is the <tt>PageContext</tt> where this tag was used.
     * @return the <tt>Object</tt> containing the result of
     * evaluating the expression.
     * @throws JspTagException if an error occurs during evaluation.
     */
    public static Object eval(String attrName, String expr, Class theClass,
                              Tag tag, PageContext pctx)
        throws JspTagException
    {
        Object obj = null;
        try {
            obj = ExpressionEvaluatorManager.evaluate
                (attrName, expr, theClass, tag, pctx);
        } catch (JspException je) {
            throw new JspTagException
                ("Could not evaluate expression \"" + expr + "\": " + je);
        }
        return obj;
    }
}
