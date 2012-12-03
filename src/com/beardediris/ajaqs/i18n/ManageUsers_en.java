/**
 * ManageUsers_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>manageusers.jsp</tt> page.</p>
 */
public class ManageUsers_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Manage Users"},
        {"banner.title", "Manage Users"},
        {"user.creation", "Created on {0}"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"link.delete", "Delete"},
        {"link.edit", "Edit"},
    };

    public ManageUsers_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
