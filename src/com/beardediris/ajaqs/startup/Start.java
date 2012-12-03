/**
 * Start.java
 */

package com.beardediris.ajaqs.startup;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

/**
 * <p>This listener should be specified in web.xml.  It initializes
 * the application by connecting to the appropriate back-end,
 * and storing the connection in an application-wide context.
 * When the application shuts down, the listener also
 * gracefully disconnects from the back-end.</p>
 *
 * <p>Note that we also load the default end-user configuration
 * here.</p>
 *
 * @see Constants
 * @see LoadConfig
 * @see com.beardediris.ajaqs.session.LoadUserConfig
 */
public class Start
    implements ServletContextListener, Constants
{
    private static final org.apache.log4j.Logger logger =
        org.apache.log4j.Logger.getLogger(Start.class.getName());

    /**
     * This method does relevant initialization when the
     * application first starts.  Mainly, we're concerned
     * here with setting up the JDO, from which Database
     * objects are later retrieved.
     */
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext ctx = ev.getServletContext();
        String dbname = ctx.getInitParameter(DBNAME);
        if (null == dbname || dbname.length() <= 0) {
            throw new RuntimeException
                ("\"" + DBNAME + "\" parameter must be specified");
        }

        String dbfile = ctx.getInitParameter(DBFILE);
        if (null == dbfile || dbfile.length() <= 0) {
            throw new RuntimeException
                ("\"" + DBFILE + "\" parameter must be specified");
        }

        String config = ctx.getInitParameter(CONFIG);
        if (null == config || config.length() <= 0) {
            throw new RuntimeException
                ("\"" + CONFIG + "\" parameter must be specified");
        }

        String sqllog = ctx.getInitParameter(SQLLOG);
        if (null == sqllog || sqllog.length() <= 0) {
            throw new RuntimeException
                ("\"" + SQLLOG + "\" parameter must be specified");
        }

        org.exolab.castor.util.Logger writer = null;
        try {
            writer = new org.exolab.castor.util.Logger
		(new FileOutputStream(sqllog));
        } catch (FileNotFoundException fnfe) {
            throw (RuntimeException)new RuntimeException
                ("Could not create writer").initCause(fnfe);
        }

        logger.info("loading dbfile: " + dbfile);
        URL uri = getClass().getResource(dbfile);
        JDOManager jdoMgr;
        try {
            JDOManager.loadConfiguration(uri.toString());
            jdoMgr = JDOManager.createInstance(dbname);
        } catch (MappingException me) {
            throw (RuntimeException)new RuntimeException
                ("Could not create Mapping object").initCause(me);
        }

        logger.info("Putting JDO into servlet context...");
        // Put JDO into the servlet context for future reference.
        ctx.setAttribute
            (com.beardediris.ajaqs.session.Constants.JDO_ATTR, jdoMgr);

        // Put properties into the servlet context for future reference.
        // Note that we really want to put these properties into the
        // session context to support user-specific configuration.
        // However, we do not as yet have a session, so we first put
        // the properties in the servlet context, and then load them
        // into individual sessions on-demand, by retrieving the
        // Properties object that is stored here whenever a new
        // session is started.
        Properties props = LoadConfig.load(ctx, config, uri.getHost());
        ctx.setAttribute
            (com.beardediris.ajaqs.session.Constants.CONFIG_ATTR, props);
    }

    /**
     * This method releases global data when the application
     * is halted.
     */
    public void contextDestroyed(ServletContextEvent ev)
    {
        ServletContext ctx = ev.getServletContext();
        ctx.removeAttribute
            (com.beardediris.ajaqs.session.Constants.JDO_ATTR);
    }
}
