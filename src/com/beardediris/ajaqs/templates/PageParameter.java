/**
 * PageParameter.java
 */

package com.beardediris.ajaqs.templates;

/**
 * <p>This class is used to hold JSP or HTML content associated
 * with some attribute in the page context.</p>
 *
 * @see PutTag
 */
public class PageParameter
{
    private String m_content;
    private String m_direct;

    public void setContent(String content) {
        m_content = content;
    }
    public String getContent() {
        return m_content;
    }

    public void setDirect(String direct) {
        m_direct = direct;
    }
    public String getDirect() {
        return m_direct;
    }

    public boolean isDirect() {
        return Boolean.valueOf(m_direct).booleanValue();
    }

    public PageParameter(String content, String direct) {
        m_content = content;
        m_direct = direct;
    }
}
