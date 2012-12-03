/**
 * Logon_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>logon.jsp</tt> page.</p>
 */
public class Logon_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Login"},
        {"banner.title", "Login to Ajaqs"},
        {"form.summary", "Enter username and password"},
        {"form.user", "User:"},
        {"form.password", "Password:"},
        {"form.submit", "Submit"},
        {"error.msg", "You entered a bad User/Password combination.  "
         + "Please try again."},
        {"link.newuser", "New User?"},
    };

    public Logon_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
