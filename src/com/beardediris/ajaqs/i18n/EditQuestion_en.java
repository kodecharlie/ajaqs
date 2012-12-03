/**
 * EditQuestion_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>editfaq.jsp</tt> page.</p>
 */
public class EditQuestion_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Edit:  {0}"},
        {"banner.title", "Edit Question"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"link.delete", "Delete"},
        {"link.edit", "Edit"},
        {"form.summary", "Edit question"},
        {"form.question", "Question:"},
        {"form.submit", "Submit"},
        {"answer.creation", "Created on {0}"},
        {"error.emptyquestion", "You submitted an empty question.  "
         + "Please enter a question and resubmit your changes."},
    };

    public EditQuestion_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
