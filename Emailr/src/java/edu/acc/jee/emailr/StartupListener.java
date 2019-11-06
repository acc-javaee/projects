package edu.acc.jee.emailr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.*;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        final String url = e.getServletContext().getInitParameter("db.url");
        final String user = e.getServletContext().getInitParameter("db.user");
        final String pass = e.getServletContext().getInitParameter("db.pass");
        
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            e.getServletContext().setAttribute("db", conn);
        }
        catch (SQLException sqle) {
            e.getServletContext().setAttribute("db", null);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        @SuppressWarnings("unchecked")
        Connection conn = (Connection)e.getServletContext().getAttribute("db");
        if (conn != null) try {conn.close();} catch (SQLException sqle){}
    }

}
