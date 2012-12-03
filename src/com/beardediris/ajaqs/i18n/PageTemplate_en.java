/**
 * PageTemplate_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>pageTemplate.jsp</tt> page.</p>
 */
public class PageTemplate_en extends ListResourceBundle
{
    private final static String MIMETYPE_COMMENT =
        "We specify the media type as text/html because the media\n"
        + "type for XHTML is still not well defined.  To make the\n"
        + "XHTML backwards compatible with HTML, we follow guidelines\n"
        + "at: http://www.w3.org/TR/xhtml1/#guidelines";

    private static Object contents[][] = {
        {"mimetype.comment", MIMETYPE_COMMENT},
    };

    public PageTemplate_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
