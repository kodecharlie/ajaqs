/**
 * EditProject_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>editproject.jsp</tt> page.</p>
 */
public class EditProject_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Edit {0}"},
        {"banner.title", "Edit {0}"},
        {"link.addfaq", "Add FAQ"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"link.delete", "Delete"},
        {"link.edit", "Edit"},
        {"form.summary", "Edit project information"},
        {"form.name", "Name:"},
        {"form.state", "State:"},
        {"form.roles", "Roles:"},
        {"form.users", "Users:"},
        {"form.submit", "Submit"},
        {"selst.active", "Active"},
        {"selst.inactive", "Inactive"},
        {"selusers.all", "All users"},
        {"faq.creation", "Created on {0}"},
        {"error.noname", "You submitted an empty name for the project.  "
         + "Please enter a name for the project and resubmit your changes."},
        {"error.nousers", "You must specify at least one user who has "
         + "permissions to view the project.  Please select one or more "
         + "users and resubmit your changes."},
        {"error.duplicate", "The name you submitted for the project "
         + "is already in use.  Please enter a different name for the "
         + "project and resubmit your changes."},
    };

    public EditProject_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
