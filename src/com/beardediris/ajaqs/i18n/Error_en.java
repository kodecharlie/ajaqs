/**
 * Error_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>error.jsp</tt> page.</p>
 */
public class Error_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Error Report"},
        {"banner.title", "Internal Error Report"},
        {"link.projects", "Projects"},
        {"link.logout", "Log out"},
        {"error.msg", "An internal error has occurred.  Please notify "
         + "our staff by saving the error report below and emailing "
         + "it to us sometime.  We apologize for any inconvenience."},
    };

    public Error_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
