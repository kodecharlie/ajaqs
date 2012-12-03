/**
 * Attach_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>attach.jsp</tt> page.</p>
 */
public class Attach_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"banner.title", "Add Attachment to Answer"},
        {"head.title", "Add Attachment to Answer"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.logout", "Log out"},
        {"form.summary", "Enter name and type of file"},
        {"form.filename", "File name"},
        {"form.filetype", "File type"},
        {"form.descr", "Description"},
        {"form.submit", "Submit"},
        {"filetype.default", "Unspecified"},
        {"error.emptyfilename", "You submitted an invalid filename.  "
         + "Please enter a valid filename for your attachment and press "
         + "submit."},
    };

    public Attach_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
