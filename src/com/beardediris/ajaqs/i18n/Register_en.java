/**
 * Register_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>register.jsp</tt> page.</p>
 */
public class Register_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Register new user"},
        {"banner.title", "Register New User"},
        {"form.summary", "Enter user information"},
        {"form.logon", "Logon:"},
        {"form.password", "Password:"},
        {"form.retype", "Re-type password:"},
        {"form.email", "E-mail:"},
        {"form.projects", "Choose projects:"},
        {"form.submit", "Submit"},
        {"selpjs.all", "All projects"},
        {"error.emptylogon", "You submitted an empty logon name. "
         + "Please enter a logon name and press submit."},
        {"error.passwords", "The two passwords you typed do not match. "
         + "Please try again."},
        {"error.duplicate", "The username you have submitted is already "
         + "taken. Please try another username."},
    };

    public Register_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
