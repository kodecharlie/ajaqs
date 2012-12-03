/**
 * Submitter.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.apache.log4j.Logger;

/**
 * <p>This class is used by servlets that need to parse
 * FORM data submitted in an HTTP POST command.</p>
 */
public abstract class Submitter extends ServeError
{
    private static final Logger logger =
        Logger.getLogger(Submitter.class.getName());

    /**
     * Called by subclasses that need to deal with parameters
     * submitted via POST, with an encoding type of multipart/form-data.
     * We assume here that no Part is a file input.
     *
     * @throws IOException
     */
    protected HashMap parseParameters(HttpServletRequest req, int maxsize)
        throws IOException
    {
        HashMap partList = new HashMap();

        MultipartParser mp = new MultipartParser(req, maxsize);
        do {
            Part part = part = mp.readNextPart();
            if (null == part) {
                break;
            }
            partList.put(part.getName(), part);
        } while (true);

        return partList;
    }

    /**
     * Parse and return an integer-valued request parameter from the
     * <tt>HttpServletRequest</tt> object.
     *
     * @param paramName holds the name of the request parameter to be
     * parsed.
     * @return the parsed integer.
     * @throws ServletException if either the named parameter is not
     * found or the integer is malformed.
     */
    protected int parseInteger(HttpServletRequest req, String paramName)
        throws ServletException
    {
        String paramValue = req.getParameter(paramName);
        if (null == paramValue || paramValue.length() <= 0) {
            throw new ServletException
                ("\"" + paramName + "\" parameter must be specified");
        }

        int retval;
        try {
            retval = Integer.parseInt(paramValue);
        } catch (NumberFormatException nfe) {
            throw (ServletException)new ServletException
                ("\"" + paramName + "\" parameter must be a valid integer")
                .initCause(nfe);
        }
        return retval;
    }

    protected String getTrimmedParameter(HttpServletRequest req,
                                         String paramName)
    {
        String paramValue = req.getParameter(paramName);
        if (null != paramValue) {
            return paramValue.trim();
        }
        return null;
    }

    /**
     * Called to forward the servlet to another URL.
     *
     * @throws ServletException
     * @throws IOException
     */
    protected void forwardRequest(Database db, String path,
                                  HttpServletRequest req,
                                  HttpServletResponse resp)
        throws ServletException, IOException
    {
        RequestDispatcher rd = req.getRequestDispatcher(path);
        if (null == rd) {
            throw new ServletException
                ("Could not get RequestDispatcher to forward request.");
        }

        // We must process this forwarding request as a new
        // transaction.  Unfortunately, the appserver cannot
        // intercept this forward request and process it within
        // the normal filtering heirarchy.
        try {
            // Begin transaction.
            db.begin();

            // Put database object into the request context
            // so that it can be accessed by JSPs that we are
            // forwarded to.  NOTE we assume that we will not
            // be forwarded to one of the Ajaqs servlets, where
            // the expectation is that a new database must be
            // retrieved from the JDO.
            req.setAttribute(DB_ATTR, db);

            // Process JSP to which we are forwarded.
            rd.forward(req, resp);

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
            req.removeAttribute(DB_ATTR);
        }
        try {
            // Close the database. This is awkward because here we
            // did not get the Database from the JDO. However,
            // we assume that calls to forwardRequest() will be
            // the final step in the invoking code. Typically,
            // this is assumed to be servlet code.
            db.close();
        } catch (PersistenceException pe) {
            throw (ServletException)new ServletException
                ("Could not close database").initCause(pe);
        }
    }
}
