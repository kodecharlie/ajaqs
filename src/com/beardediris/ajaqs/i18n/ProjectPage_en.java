/**
 * ProjectPage_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>projectpage.jsp</tt> page.</p>
 */
public class ProjectPage_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Browse FAQs for {0}"},
        {"banner.title", "FAQs for {0}"},
        {"search.label", "Key words:"},
        {"search.submit", "Search {0}"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.question", "Submit question"},
        {"link.logout", "Log out"},
        {"link.search", "Search"},
    };

    public ProjectPage_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
