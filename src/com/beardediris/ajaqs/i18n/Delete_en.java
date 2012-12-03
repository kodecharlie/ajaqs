/**
 * Delete_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>delete.jsp</tt> page.</p>
 */
public class Delete_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Delete {0}"},
        {"banner.title", "Delete {0}"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"delete.request", "You have requested to delete the following {0}:"},
        {"delete.confirm", "Are you certain you wish to delete this {0}?"},
        {"form.submit", "Delete"},
        {"error.msg", "An error occurred while deleting the {0}.  Please try again."},
    };

    public Delete_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
