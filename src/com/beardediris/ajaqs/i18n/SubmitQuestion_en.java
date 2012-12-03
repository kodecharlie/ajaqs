/**
 * SubmitQuestion_en.java
 */

package com.beardediris.ajaqs.i18n;

import java.util.ListResourceBundle;

/**
 * <p>This class implements the English resource-bundle used for
 * the <tt>submitquestion.jsp</tt> page.</p>
 */
public class SubmitQuestion_en extends ListResourceBundle
{
    private static Object contents[][] = {
        {"head.title", "Submit question"},
        {"banner.title", "Submit a Question for {0}"},
        {"link.projects", "Projects"},
        {"link.browse", "Browse all"},
        {"link.logout", "Log out"},
        {"inputs.summary", "Submit question and answer"},
        {"selfaq.label", "Choose an FAQ"},
        {"question.ask", "Ask a question"},
        {"question.answer", "Answer the question (optional)"},
        {"question.submit", "Submit question"},
        {"error.emptyquestion", "The question you submitted was empty.  "
         + "Please enter your question and resubmit it."},
    };

    public SubmitQuestion_en() {
        // empty
    }

    public Object[][] getContents() {
        return contents;
    }
}
