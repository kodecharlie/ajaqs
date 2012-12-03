/**
 * LoadUserConfig.java
 */

package com.beardediris.ajaqs.session;

import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;

/**
 * <p>This class monitors the beginning and ending of each
 * end-user session.  When a session is first started,
 * this listener initializes various end-user configuration
 * in the session context; that is, the listener defines
 * several attributes in the session context that encompass
 * end-user configuration.  This listener assumes the
 * existence in the application context of an attribute
 * containing a <code>Properties</code> object containing
 * default end-user configuration.</p>
 */
public class LoadUserConfig
    implements HttpSessionListener, Constants
{
    private static final Logger logger =
        Logger.getLogger(LoadUserConfig.class.getName());

    /**
     * Populate the session with attributes containing the
     * default end-user configuration.  Note that eventually
     * we will load specific end-user configuration and
     * override defaults, if need be; this feature, is not
     * yet supported.
     *
     * @param sev contains a <code>HttpSessionEvent</code>
     * created when the session first starts.
     */
    public void sessionCreated(HttpSessionEvent sev)
    {
        HttpSession session = sev.getSession();
        ServletContext sctx = session.getServletContext();

        Properties config = (Properties)sctx.getAttribute(CONFIG_ATTR);
        if (null == config) {
            throw new RuntimeException
                ("\"" + CONFIG_ATTR + "\" attribute not found "
                 + "in servlet context.");
        }

        Enumeration en = config.keys();
        while (en.hasMoreElements()) {
            String key = (String)en.nextElement();
            Object value = config.get(key);
            session.setAttribute(key, value);
        }
    }

    /**
     * This is a noop.
     */
    public void sessionDestroyed(HttpSessionEvent sev)
    {
        // empty
    }
}
