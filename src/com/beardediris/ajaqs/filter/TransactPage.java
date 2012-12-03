/**
 * TransactPage.java
 */

package com.beardediris.ajaqs.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.beardediris.ajaqs.session.Constants;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>Start and end a database-transaction, via Castor.
 * The transaction is scoped across each JSP that
 * is filtered by this class.  Note that we could
 * incur some performance penalty during multi-user
 * processing, as a Castor-transaction could lock
 * out other database queries.  The trade-off is that
 * JSP/taglib development can ignore transaction
 * issues so long as the transaction is opened and closed
 * here.</p>
 * <p>See web.xml for how this filter is configured with
 * respect to JSPs.</p>
 */
public class TransactPage
    implements Filter, Constants
{
    private static final Logger logger =
        Logger.getLogger(TransactPage.class.getName());

    private FilterConfig config;

    public void init(FilterConfig config) {
        this.config = config;
    }

    public void destroy() {
        // empty
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException
    {
        ServletContext ctx = config.getServletContext();
        JDOManager jdo = (JDOManager)ctx.getAttribute
            (com.beardediris.ajaqs.session.Constants.JDO_ATTR);
        if (null == jdo) {
            throw new ServletException
                ("JDO not found in application context");
        }

        Database db = null;
        try {
            db = jdo.getDatabase();
        } catch (DatabaseNotFoundException dnfe) {
            throw (ServletException)new ServletException
                ("Could not get Database from JDO").initCause(dnfe);
        } catch (PersistenceException pe) {
            throw (ServletException)new ServletException
                ("Could not get Database from JDO").initCause(pe);
        }

        try {
            // Begin transaction.
            db.begin();

            // Put the database object into the request context
            // so that it can be accessed by any JSPs that are
            // processed under this filter.
            request.setAttribute(DB_ATTR, db);

            // Process request.
            chain.doFilter(request, response);

            // End transaction.
            db.commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            try {
                // Rollback the transaction.
                db.rollback();
            } catch (TransactionNotInProgressException tnpe) {
                // We do not care.
                logger.info("could not rollback transaction: " + tnpe);
            }
        } finally {
            // Remove database object from request context.
            request.removeAttribute(DB_ATTR);
        }
        try {
            // Close the database.
            db.close();
        } catch (PersistenceException pe) {
            throw (ServletException)new ServletException
                ("Could not close database").initCause(pe);
        }
    }
}
