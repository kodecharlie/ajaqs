/**
 * LoadConfig.java
 */

package com.beardediris.ajaqs.startup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.SecurityException;
import java.util.Properties;
import javax.servlet.ServletContext;
import com.beardediris.ajaqs.session.Constants;

/**
 * <p>This class loads the default end-user configuration.</p>
 */
public class LoadConfig implements Constants
{
    /**
     * Set up (property, value) pairs that specify defaults
     * for various configuration options.
     *
     * @param sctx contains the <code>ServletContext</code>
     * of the web application that invoked this method.
     * @param config specifies resource containing properties
     * file with end-user configuration.
     * @return <code>Properties</code> containing default
     * end-user configuration.
     */
    public static Properties load(ServletContext sctx, String config,
                                  String serverName)
    {
        // Load default configuration properties.  Some or all of
        // these may be overwritten when we load the end-user
        // configuration file below.
        Properties props = new Properties();
        props.setProperty(PAGE_SIZE, "10");
        props.setProperty(SHOW_INACTIVE_USERS, "true");
        props.setProperty(SHOW_INACTIVE_PROJECTS, "true");
        props.setProperty(SHOW_INACTIVE_FAQS, "true");
        props.setProperty(SERVER_NAME, serverName);

        // Load configuration properties.
        try {
            URL confUri = sctx.getResource(config);
            props.load(confUri.openStream());
        } catch (MalformedURLException mue) {
            throw (RuntimeException)new RuntimeException
                ("Could not load configuration properties").initCause(mue);
        } catch (SecurityException se) {
            throw (RuntimeException)new RuntimeException
                ("Could not load configuration properties").initCause(se);
        } catch (IOException ioe) {
            throw (RuntimeException)new RuntimeException
                ("Could not load configuration properties").initCause(ioe);
        }
        return props;
    }
}
