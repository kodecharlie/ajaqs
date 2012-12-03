/**
 * MapDB.java
 */

package com.beardediris.ajaqs.startup;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

/**
 * <p>This servlet should be loaded only once when the web
 * application is first launched, and never again.  It is
 * recommended that you do this initialization in a
 * ServletContextListener.  But if your webapp container
 * does not support listeners, you should use this
 * servlet to initialize the application.  Be forewarned
 * that although this code is maintained in sync with
 * changes to <tt>Start.java</tt>, it is not routinely
 * tested; in another words, use it at your own risk.</p>
 *
 * <p>Create the Castor JDO and put it into the ServletContext,
 * as it is required on an application-wide basis (i.e.,
 * not used simply for each HttpSession).</p>
 *
 * <p>Note that we also load the default end-user configuration
 * here.</p>
 *
 * @see Constants
 * @see LoadConfig
 * @see com.beardediris.ajaqs.session.LoadUserConfig
 */
public final class MapDB extends HttpServlet
    implements Constants
{
    private static final org.apache.log4j.Logger logger =
        org.apache.log4j.Logger.getLogger(MapDB.class.getName());

    /**
     * Maps Java objects to tables in the back-end.  Stores
     * JDO connection in application-wide context.
     */
    public void init()
        throws ServletException
    {
        String dbname = getInitParameter(DBNAME);
        if (null == dbname || dbname.length() <= 0) {
            throw new ServletException
                ("\"" + DBNAME + "\" parameter must be specified");
        }

        String dbfile = getInitParameter(DBFILE);
        if (null == dbfile || dbfile.length() <= 0) {
            throw new ServletException
                ("\"" + DBFILE + "\" parameter must be specified");
        }

        String config = getInitParameter(CONFIG);
        if (null == config || config.length() <= 0) {
            throw new ServletException
                ("\"" + CONFIG + "\" parameter must be specified");
        }

        String sqllog = getInitParameter(SQLLOG);
        if (null == sqllog || sqllog.length() <= 0) {
            throw new ServletException
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
            throw (ServletException)new ServletException
                ("Could not create Mapping object").initCause(me);
        }

        logger.info("Putting JDO into servlet context...");
        // Put JDO into the servlet context for future reference.
        ServletContext ctx = getServletContext();
        ctx.setAttribute
            (com.beardediris.ajaqs.session.Constants.JDO_ATTR, jdoMgr);

        // Put properties into the servlet context for future reference.
        // See Start.java for further explanation.
        Properties props = LoadConfig.load(ctx, config, uri.getHost());
        ctx.setAttribute
            (com.beardediris.ajaqs.session.Constants.CONFIG_ATTR, props);
    }

    /**
     * This method should never be called.
     *
     * @throws ServletException always does this.
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        throw new ServletException
            ("Cannot invoke this servlet via HTTP GET.");
    }

    /**
     * This method should never be called.
     *
     * @throws ServletException always does this.
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        throw new ServletException
            ("Cannot invoke this servlet via HTTP POST.");
    }
}
