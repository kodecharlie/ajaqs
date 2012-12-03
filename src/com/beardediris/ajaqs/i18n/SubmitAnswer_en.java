/**
 * SubmitAnswer_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>submitanswer.jsp</tt> page.</p>
 */
public class SubmitAnswer_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Submit answer"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.logout", "Log out"},
        {"inputs.summary", "Input new answer"},
        {"inputs.textarea", "Enter new answer"},
        {"inputs.submit", "Submit answer"},
        {"error.emptyanswer", "The answer you submitted was empty.  "
         + "Please enter your answer and resubmit it."},
    };

    public SubmitAnswer_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
