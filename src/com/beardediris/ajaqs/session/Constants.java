/**
 * Constants.java
 */

package com.beardediris.ajaqs.session;

/**
 * <p>This interface specifies constants used on a session-wide
 * basis.</p>
 *
 * @see com.beardediris.ajaqs.oql.QueryDB
 */
public interface Constants {
    /**
     * This attribute in fact is scoped to JSP pages.
     * It is expected to hold the Database object during
     * JSP processing.
     */
    public final static String DB_ATTR =
        "com.beardediris.ajaqs.session.castorDB";

    /**
     * This attribute in fact is application-wide (ie,
     * associated data is stored in the ServletContext),
     * but since it is used by all sessions, we define it
     * here. Associated with this attribute is the JDO.
     */
    public final static String JDO_ATTR =
        "com.beardediris.ajaqs.session.JDO";

    /**
     * This attribute exists on a per-session basis. It
     * is created when the session is first created, and
     * later re-used to record the user's login-time when
     * the session terminates.
     */
    public final static String RECORD_LOGIN_TIME =
        "com.beardediris.ajaqs.session.recordLoginTime";

    /**
     * This attribute describes configuration properties loaded
     * when the application is first started.  This configuration
     * is stored in the application context, but then loaded
     * on-demand into each new end-user session.
     */
    public final static String CONFIG_ATTR =
        "com.beardediris.ajaqs.session.config";

    /**
     * This is the scrolling increment for certain JSPs, ie,
     * the number of search results, projects, FAQs (or similar
     * items) that can be displayed on a single page at any one time.
     */
    public final static String PAGE_SIZE =
        "com.beardediris.ajaqs.session.pageSize";

    /**
     * This is a boolean user-option that determines whether
     * information on inactivated users is visible.
     */
    public final static String SHOW_INACTIVE_USERS =
        "com.beardediris.ajaqs.session.showInactiveUsers";

    /**
     * This is a boolean user-option that determines whether
     * inactivated projects are visible.
     */
    public final static String SHOW_INACTIVE_PROJECTS =
        "com.beardediris.ajaqs.session.showInactiveProjects";

    /**
     * This is a boolean user-option that determines whether
     * inactivated FAQs are visible.
     */
    public final static String SHOW_INACTIVE_FAQS =
        "com.beardediris.ajaqs.session.showInactiveFaqs";

    /**
     * This property contains a domain like 'www.smattworks.com',
     * which is used to server up RSS feeds.
     */
    public final static String SERVER_NAME =
        "com.beardediris.ajaqs.session.serverName";

    /**
     * We use this attribute to store exceptions in the page
     * request in order to display error.jsp along with meaningful
     * information related exceptions that triggered the error.
     */
    public final static String INTERNAL_EXCEPTION =
        "com.beardediris.ajaqs.session.internalException";

    /**
     * This is the size-limitation for questions uploaded
     * to Ajaqs.  In MySql, this corresponds to the size
     * of a BLOB type.  This probably should be an option,
     * as it could be database-dependent.
     */
    public final static int SIXTYFOUR_KB = 64 * 1024;
}
