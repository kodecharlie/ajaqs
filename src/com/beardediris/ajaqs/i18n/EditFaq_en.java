/**
 * EditFaq_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>editfaq.jsp</tt> page.</p>
 */
public class EditFaq_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Edit {0} -> {1}"},
        {"banner.title", "Edit {0} -> {1}"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"link.delete", "Delete"},
        {"link.edit", "Edit"},
        {"form.summary", "Edit FAQ information"},
        {"form.name", "Name:"},
        {"form.state", "State:"},
        {"form.submit", "Submit"},
        {"selst.active", "Active"},
        {"selst.inactive", "Inactive"},
        {"question.creation", "Created on {0}"},
        {"error.noname", "You submitted an empty name for the FAQ.  "
         + "Please enter a name for the FAQ and resubmit your changes."},
        {"error.duplicate", "The name you submitted for the FAQ "
         + "is already in use.  Please enter a different name for the "
         + "FAQ and resubmit your changes."},
    };

    public EditFaq_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
