/**
 * Logout.java
 */

package com.beardediris.ajaqs.util;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * <p>This servlet is used to terminate an end-user session.
 * It should be invoked whenever the end-user requests to logout.</p>
 */
public class Logout extends HttpServlet
{
    private static final Logger logger =
        Logger.getLogger(Logout.class.getName());

    /**
     * On successful completion, go to this JSP.
     */
    private final static String FORWARD_URL =
        "/jsp/security/logoff.jsp";

    /**
     * Invalidate the session, and forward the end-user to a
     * signout page.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // Invalidate the current session.
        HttpSession session = req.getSession();
        session.invalidate();

        // Forward request. Notice that we do not need to do this
        // within the confines of a castor transaction because the
        // logoff page should not contain any information that is
        // queried from the backend.
        RequestDispatcher rd = req.getRequestDispatcher(FORWARD_URL);
        if (null == rd) {
            throw new ServletException
                ("Could not get RequestDispatcher to forward request.");
        }
        rd.forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }
}
