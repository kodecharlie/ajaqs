/**
 * User_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>user.jsp</tt> page.</p>
 */
public class User_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Profile for {0}"},
        {"banner.title", "Profile for {0}"},
        {"link.projects", "Projects"},
        {"link.logout", "Log out"},
        {"info.summary", "End-user profile"},
        {"info.logon", "User"},
        {"info.email", "E-mail"},
        {"info.lastLogin", "Last login"},
        {"info.state", "State"},
        {"info.state.active", "Active"},
        {"info.state.inactive", "Inactive"},
        {"info.projects", "Projects of interest"},
    };

    public User_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
