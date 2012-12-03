/**
 * NewFaq_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>newfaq.jsp</tt> page.</p>
 */
public class NewFaq_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Add FAQ to {0}"},
        {"banner.title", "Add FAQ to {0}"},
        {"link.projects", "Projects"},
        {"link.users", "Users"},
        {"link.logout", "Log out"},
        {"form.summary", "Enter information for new FAQ"},
        {"form.name", "Name of FAQ:"},
        {"form.submit", "Submit"},
        {"error.noname", "You did not specify a name for the new FAQ.  "
         + "Please enter a name for the FAQ and press submit."},
        {"error.duplicate", "The name you specified for the new FAQ "
         + "is already in use for the project \"{0}\".  Please enter "
         + "a different name for the new FAQ and press submit."},
    };

    public NewFaq_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
