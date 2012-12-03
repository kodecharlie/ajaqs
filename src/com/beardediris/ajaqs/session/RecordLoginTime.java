/**
 * RecordLoginTime.java
 */

package com.beardediris.ajaqs.session;

import java.io.Serializable;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This class is responsible for updating the time
 * of the most recent login for the current user.  The
 * update is performed when the end-user session terminates.
 * We make <tt>RecordLoginTime</tt> serializable because it
 * is stored in the session, and we want all HTTP session
 * data to be commutable in a clustered environment.</p>
 */
public class RecordLoginTime 
    implements HttpSessionBindingListener, Serializable
{
    private static final Logger logger =
        Logger.getLogger(RecordLoginTime.class.getName());

    private String m_logon;
    private long m_creationTime;

    public RecordLoginTime(String logon) {
        m_logon = logon;
    }

    public void valueBound(HttpSessionBindingEvent ev)
    {
        HttpSession session = ev.getSession();
        m_creationTime = session.getCreationTime();
    }

    public void valueUnbound(HttpSessionBindingEvent ev)
    {
        if (null == m_logon) {
            // Evidently the session was aborted soon after having
            // started.  In that case, we do not bother to record
            // the login date.
            return;
        }

        QueryDB query = null;
        try {
            query = new QueryDB(ev.getSession().getServletContext());
        } catch (DatabaseNotFoundException dnfe) {
            throw (RuntimeException)new RuntimeException
                ("Could not set lastLoginDate of user").initCause(dnfe);
        }

        Database db = query.getDb();
        try {
            // Begin transaction.
            db.begin();

            logger.info("creationTime=" + m_creationTime);
            FaqUser fuser = (FaqUser)query.getFaqUser(m_logon, false);
            Date lastLoginDate = new Date(m_creationTime);
            fuser.setLastLoginDate(lastLoginDate);
            logger.info("lastLoginDate=" + lastLoginDate.toString());

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
        }
        try {
            // Close the database.
            db.close();
        } catch (PersistenceException pe) {
            throw (RuntimeException)new RuntimeException
                ("Could not close database").initCause(pe);
        }
    }

    public String getLogon() {
        return m_logon;
    }
    public void setLogon(String logon) {
        m_logon = logon;
    }

    public long getCreationTime() {
        return m_creationTime;
    }
    public void setCreationTime(long creationTime) {
        m_creationTime = creationTime;
    }
}
