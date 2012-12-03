/**
 * Traversal_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>traversal.jsp</tt> page.</p>
 */
public class Traversal_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"previous", "Previous"},
        {"next", "Next"},
    };

    public Traversal_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
