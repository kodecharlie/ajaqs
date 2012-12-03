/**
 * ManageProjects_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>manageprojects.jsp</tt> page.</p>
 */
public class ManageProjects_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Manage Projects"},
        {"banner.title", "Manage Projects"},
        {"project.creation", "Created on {0}"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.newproject", "New Project"},
        {"link.logout", "Log out"},
        {"link.delete", "Delete"},
        {"link.edit", "Edit"},
    };

    public ManageProjects_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
