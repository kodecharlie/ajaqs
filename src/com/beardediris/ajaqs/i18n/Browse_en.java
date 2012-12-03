/**
 * Browse_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>browse.jsp</tt> page.</p>
 */
public class Browse_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Browse {0} FAQs"},
        {"banner.title", "Browse {0} FAQs"},
        {"search.label", "Key words:"},
        {"search.submit", "Search {0}"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.question", "Submit question"},
        {"link.logout", "Log out"},
        {"answer.summary", "Submitter and attachments"},
        {"answer.submitter", "Submitted by {0} on {1}"},
        {"answer.attachments", "Attachments:"},
        {"answer.new", "Submit new answer"},
        {"attachment.add", "Add"},
    };

    public Browse_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
