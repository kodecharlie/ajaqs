/**
 * Logoff_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>logoff.jsp</tt> page.</p>
 */
public class Logoff_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Logout"},
        {"banner.title", "Thanks for using Ajaqs"},
        {"login.msg", "Log back in"},
    };

    public Logoff_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
