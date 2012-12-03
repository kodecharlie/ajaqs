/**
 * Util.java
 */

package com.beardediris.ajaqs.util;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;
import com.beardediris.ajaqs.session.Constants;

/**
 * <p>This class contains static methods used for a variety of purposes.</p>
 *
 * @see com.beardediris.ajaqs.taglib.EmphasizeTag
 * @see com.beardediris.ajaqs.taglib.GetOrSetTag
 * @see com.beardediris.ajaqs.taglib.GetPropertyTag
 */
public class Util implements Constants
{
    private final static Logger logger =
        Logger.getLogger(Util.class.getName());

    private final static String PUNCTUATION = "\\p{Punct}";
    private final static String WS_SEQUENCE = "\\s+";
    private final static String SPACE = " ";

    private final static String APPLICATION = "application";
    private final static String SESSION = "session";
    private final static String REQUEST = "request";
    private final static String PAGE = "page";

    /**
     * Convert a <code>String</code> specifying the scope into a
     * integer-valued code understood by JSPs and servlets.
     *
     * @param scope specifies one of the following scopes:
     * <ul>
     * <li>application</li>
     * <li>session</li>
     * <li>request</li>
     * <li>page</li>
     * </ul>
     * @param tagName name of the tag which led to this method call.
     * @param attr name of attribute of tag used to specify scope.
     * @return an <code>int</code> indicating the type of scope.
     * @throws JspTagException in the event that <code>scope</code>
     * does not contain an acceptable value.
     */
    public static int getScope(String tagName, String scope, String attr)
        throws JspTagException
    {
        int scopeVal;
        if (PAGE.equalsIgnoreCase(scope)) {
            scopeVal = PageContext.PAGE_SCOPE;
        } else if (SESSION.equalsIgnoreCase(scope)) {
            scopeVal = PageContext.SESSION_SCOPE;
        } else if (REQUEST.equalsIgnoreCase(scope)) {
            scopeVal = PageContext.REQUEST_SCOPE;
        } else if (APPLICATION.equalsIgnoreCase(scope)) {
            scopeVal = PageContext.APPLICATION_SCOPE;
        } else {
            throw new JspTagException
                (tagName + ": bad value \"" + scope
                 + "\" given for \"" + attr + "\"");
        }
        return scopeVal;
    }

    /**
     * Use Java class reflection to get the property associated with
     * some object.  If the object does not have a method such as
     * <code>Object.getProperty()</code>, then we try to invoke
     * <code>Object.property()</code>.  If that method does not exist
     * either, we throw an exception.
     *
     * @param obj contains the <code>Object</code> for which we
     * wish to retrieve the property.
     * @param property names property from <code>Object</code>.
     * @return an <code>Object</code> that contains the specified
     * property associated with <code>obj</code>.
     * @throws JspTagException in the event retrieval of the property
     * does not work.
     */
    public static Object getProperty(Object obj, String property)
        throws JspTagException
    {
        // Assume the method-name for the getter is compliant
        // with Java Bean standards, ie, is prefixed with "get".
        // If such a method is not available, then try to find
        // a method name that is equal to the name of the property,
        // ie, without the "get" prefix.
        Method method = null;
        try {
            StringBuffer name = new StringBuffer("get");
            name.append(property.substring(0, 1).toUpperCase());
            name.append(property.substring(1));
            logger.info("Try method name=" + name.toString());
            method = obj.getClass().getMethod(name.toString());
        } catch (NoSuchMethodException nsme) {
            // We do not capitalize method name.
            logger.info("Try method name without get=" + property);
            try {
                method = obj.getClass().getMethod(property);
            } catch (NoSuchMethodException nsme2) {
                // Now we really are out of luck, so return exception.
                throw (JspTagException)new JspException
                    ("Could not get property value").initCause(nsme2);
            } catch (SecurityException se) {
                throw (JspTagException)new JspException
                    ("Could not get property value").initCause(se);
            }
        } catch (SecurityException se) {
            throw (JspTagException)new JspException
                ("Could not get property value").initCause(se);
        }

        // Use Java bean reflection to get property.
        Object propertyValue = null;
        try {
            propertyValue = method.invoke(obj);
        } catch (IllegalAccessException iacce) {
            throw (JspTagException)new JspException
                ("Could not get property value").initCause(iacce);
        } catch (IllegalArgumentException iarge) {
            throw (JspTagException)new JspException
                ("Could not get property value").initCause(iarge);
        } catch (InvocationTargetException ite) {
            throw (JspTagException)new JspException
                ("Could not get property value").initCause(ite);
        } catch (ClassCastException cce) {
            // This bizarre exception occurs when the name of the
            // given propety does not match any method in the
            // object associated with the id.
            throw (JspTagException)new JspException
                ("Could not get property value. "
                 + "Check name of property.").initCause(cce);
        }
        return propertyValue;
    }

    /**
     * Decompose a <code>String</code> of text into its constituent
     * words.
     *
     * @param keywords contains text that the user has entered for
     * a search.
     * @return a <code>Collection</code> of keywords to be searched,
     * but first remove all characters used for punctuation.  We also
     * choose to remove words that contain only one letter.
     */
    public static Collection getKeywordList(String keywords) {
        // Remove punctuation, then collapse sequences of whitespace
        // into a single space.
        keywords.replaceAll(PUNCTUATION, SPACE);
        keywords.replaceAll(WS_SEQUENCE, SPACE);

        // Tokenize according to space-delimiter.
        ArrayList kwList = new ArrayList();
        StringTokenizer st = new StringTokenizer(keywords, SPACE);
        while (st.hasMoreTokens()) {
            String kw = st.nextToken();
            if (kw.length() > 1) {
                kwList.add(kw);
            }
        }
        return kwList;
    }

    /**
     * Return the regular expression '(s0|s1|...|sN)', in which
     * each string sK is the Kth string in the specified collection.
     *
     * @param coln contains a <code>Collection</code>
     * of <code>String </code>s.
     * @return <code>String </code> containing the regular expression.
     */
    public static String getRegex(Collection coln) {
        StringBuffer regex = new StringBuffer("\\b(");
        Iterator it = coln.iterator();
        int cnt = 0;
        while (it.hasNext()) {
            String s = (String)it.next();
            if (cnt++ > 0)
                regex.append("|");
            regex.append(s);
        }
        regex.append(")\\b");
        return regex.toString();
    }
}
