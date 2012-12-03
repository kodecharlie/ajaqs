/**
 * SearchMatch.java
 */

package com.beardediris.ajaqs.oql;

import java.io.Serializable;
import com.beardediris.ajaqs.db.Question;

/**
 * <p>This class is used in searches.  It associates a question
 * with a string, which is part (or all) of some answer.</p>
 */
public class SearchMatch
    implements Serializable
{
    private Question m_question;
    private String m_match;

    public SearchMatch(Question q, String m) {
        m_question = q;
        m_match = m;
    }

    public Question getQuestion() {
        return m_question;
    }
    public void setQuestion(Question question) {
        m_question = question;
    }

    public String getMatch() {
        return m_match;
    }
    public void setMatch(String match) {
        m_match = match;
    }
}
