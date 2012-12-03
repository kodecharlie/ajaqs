/**
 * ProjectList_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>projectlist.jsp</tt> page.</p>
 */
public class ProjectList_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "List of Projects"},
        {"banner.title", "Projects for {0}"},
        {"banner.subtitle", "Last login: {0}"},
        {"search.label", "Key words:"},
        {"search.submit", "Search all Projects"},
        {"link.logout", "Log out"},
        {"link.browse", "Browse all"},
        {"link.search", "Search"},
    };

    public ProjectList_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
