/**
 * NewProject_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>newproject.jsp</tt> page.</p>
 */
public class NewProject_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "New Project"},
        {"banner.title", "New Project"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"form.summary", "Enter project information"},
        {"form.name", "Name:"},
        {"form.users", "Users:"},
        {"form.submit", "Submit"},
        {"selusrs.all", "All users"},
        {"error.noname", "You did not specify a name for the new project.  "
         + "Please enter a name for the project and press submit."},
        {"error.nousers", "You must specify at least one user who has "
         + "permissions to view the project.  Please select one or more "
         + "users and press submit."},
        {"error.duplicate", "The name you specified for the new project "
         + "is already in use.  Please enter a different name for the "
         + "new project and press submit."},
    };

    public NewProject_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
