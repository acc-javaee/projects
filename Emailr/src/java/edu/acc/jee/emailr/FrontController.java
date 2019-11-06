package edu.acc.jee.emailr;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class FrontController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action"), destination;
        if (action == null || action.length() < 1) {
            action = "subscribe";
        }
        switch (action) {
            case "subscribe":
                destination = subscribe(request);
                break;
            case "unsubscribe":
                destination = unsubscribe(request);
                break;
            case "admin":
                destination = admin(request);
                break;
            default:
                destination = "subscribe";
        }

        request.getRequestDispatcher(destination + ".jsp")
                .forward(request, response);
    }

    protected String subscribe(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return "subscribe";
        }
        String email = request.getParameter("email");
        email = email.trim().toLowerCase();
        if (!email.matches(".+@.+\\..+")) {
            request.setAttribute("flash", "Invalid email address");
            return "subscribe";
        }
        if (add(email))
            request.setAttribute("success", "Thank you for subscribing!");
        else
            request.setAttribute("flash", "Unable to subscribe at this time.");
        return "subscribe";
    }

    protected String unsubscribe(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return "unsubscribe";
        }
        String email = request.getParameter("email");
        email = email.trim().toLowerCase();
        if (!email.matches("^.+@.+\\..+$")) {
            request.setAttribute("flash", "Invalid email address");
            return "unsubscribe";
        }
        if (remove(email))
            request.setAttribute("success", "You have unsubscribed. We'll miss you!");
        else
            request.setAttribute("flash", "Unable to unsubscribe you at this time.");
        return "unsubscribe";
    }

    protected String admin(HttpServletRequest request) {
        List<Email> emails = selectAll();
        request.setAttribute("emails", emails);
        return "admin";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @SuppressWarnings("unchecked")
    private Connection getDb() {
        return (Connection)this.getServletContext().getAttribute("db");
    }
    
    private boolean add(String email) {
        Connection conn = getDb();
        if (conn == null) return false;
        String sql = "SELECT added FROM emails WHERE address = ?";
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, email);
            try (ResultSet rs = pstat.executeQuery()) {
                if (!rs.next()) {
                    sql = "INSERT INTO emails (address) VALUES (?)";
                    try (PreparedStatement pstat2 = conn.prepareStatement(sql)) {
                        pstat2.setString(1, email);
                        pstat2.executeUpdate();
                        return true;
                    }
                }
                sql = "UPDATE emails SET subscribed = TRUE WHERE address = ?";
                try (PreparedStatement pstat2 = conn.prepareStatement(sql)) {
                    pstat2.setString(1, email);
                    pstat2.executeUpdate();
                    return true;
                }
            }
        }
        catch (SQLException sqle) {
            return false;
        }
    }
    
    private boolean remove(String email) {
        Connection conn = getDb();
        if (conn == null) return false;
        String sql = "SELECT added FROM emails WHERE address = ?";
        try (PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, email);
            try (ResultSet rs = pstat.executeQuery()) {
                if (!rs.next()) {
                    sql = "INSERT INTO emails (address, subscribed) VALUES (?, FALSE)";
                    try (PreparedStatement pstat2 = conn.prepareStatement(sql)) {
                        pstat2.setString(1, email);
                        pstat2.executeUpdate();
                        return true;
                    }
                }
                sql = "UPDATE emails SET subscribed = FALSE WHERE address = ?";
                try (PreparedStatement pstat2 = conn.prepareStatement(sql)) {
                    pstat2.setString(1, email);
                    pstat2.executeUpdate();
                    return true;
                }
            }
        }
        catch (SQLException sqle) {
            return false;
        }
    }
    
    private List<Email> selectAll() {
        List<Email> emails = new ArrayList<>();
        Connection conn = getDb();
        if (conn == null) return emails;
        final String sql = "SELECT * FROM emails ORDER BY address";
        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                Email email = new Email();
                email.setAddress(rs.getString("address"));
                email.setAdded(rs.getTimestamp("added"));
                email.setSubscribed(rs.getBoolean("subscribed"));
                emails.add(email);
            }
        }
        catch (SQLException sqle) {}
        return emails;
    }
}
