/**
 * Answer_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>answer.jsp</tt> page.</p>
 */
public class Answer_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"banner.subtitle", "Last answer: {0}"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.logout", "Log out"},
        {"answer.summary", "Submitter and attachments"},
        {"answer.submitter", "Submitted by {0} on {1}"},
        {"answer.attachments", "Attachments:"},
        {"answer.new", "Submit new answer"},
        {"attachment.add", "Add"},
    };

    public Answer_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
