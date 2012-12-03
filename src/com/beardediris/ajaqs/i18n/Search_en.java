/**
 * Search_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>search.jsp</tt> page.</p>
 */
public class Search_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Search Results"},
        {"banner.title", "Search Results"},
        {"banner.subtitle", "Searched {0} for {1}"},
        {"all.faqs", "All FAQs"},
        {"search.label", "Key words:"},
        {"search.submit", "Search {0}"},
        {"result.summary", "Results {0} - {1} of {2}."},
        {"result.none", "No results found."},
        {"result.lastUpdate", "Last Update: {0} by {1}."},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.logout", "Log out"},
    };

    public Search_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
