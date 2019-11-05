package edu.acc.jee.hubbub.domain;

import edu.acc.jee.hubbub.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletContext;

public class DerbyDAO implements DataService {
    private final DataSource ds;
    
    public DerbyDAO(ServletContext ctx) {
        String brand = ctx.getInitParameter("db.brand");
        String host = ctx.getInitParameter("db.host");
        String name = ctx.getInitParameter("db.database");
        String user = ctx.getInitParameter("db.user");
        String pass = ctx.getInitParameter("db.pass");
        String jdbcUrl = String.format("jdbc:%s://%s/%s", brand, host, name);
        ds = new DataSource(jdbcUrl, user, pass);
    }

    @Override
    public User addUser(UserDTO bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User findUserByUserDTO(UserDTO bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User findUserByUsername(String username) {
    /*    final String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, username);
            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                int profileId = rs.getInt("profile");
                Profile profile = 
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPasshash(rs.getString("passhash"));
                user.setJoined(rs.getDate("joined"));
                user.
            }
        } catch (SQLException sqle) {
            
        }
    */
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean userExists(String username) {
        return findUserByUsername(username) != null;
    }

    @Override
    public Post addPost(String content, User author) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Post findPostById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Post> findPostsByPage(int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateProfileFor(User user, Profile changed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment addComment(User user, Post target, String content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
