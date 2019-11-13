package edu.acc.jee.hubbub.domain;

import edu.acc.jee.hashtool.HashTool;
import edu.acc.jee.hubbub.UserDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        if (!bean.isValid())
            throw new IllegalArgumentException("Bean does not validate");
        if (this.userExists(bean.getUsername()))
            throw new IllegalArgumentException("Username " + bean.getUsername() + " is unavailable");
        String hash = HashTool.hash(bean.getPassword());
        final String sql = "INSERT INTO users (username, passhash, joined, profile) VALUES (?,?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            try {
                Profile profile = this.addProfile();
                User user = new User(bean.getUsername(), hash, profile.getId());                
                pstat.setString(1, user.getUsername());
                pstat.setString(2, user.getPasshash());
                pstat.setDate(3, new java.sql.Date(user.getJoined().getTime()));
                pstat.setInt(4, user.getProfileId());
                pstat.executeUpdate();
                conn.commit();
                return user;
            }
            catch (SQLException sqle) {
                conn.rollback();
                return null;
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    public Profile addProfile() {
        final String sql = "INSERT INTO profiles (firstName) VALUES (NULL)";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstat.executeUpdate();
            try (ResultSet rs = pstat.getGeneratedKeys()) {
                rs.next();
                Profile profile = new Profile();
                profile.setId(rs.getInt(1));
                return profile;
            }
        }
        catch (SQLException sqle) {
            return null;
        }        
    }
    
    @Override
    public User findUserByUserDTO(UserDTO bean) {
        if (!bean.isValid())
            throw new IllegalArgumentException("Bean does not validate");
        User user = this.findUserByUsername(bean.getUsername());
        if (user == null) return null;
        boolean match = HashTool.compare(bean.getPassword(), user.getPasshash());
        return match ? user : null;        
    }

    @Override
    public User findUserByUsername(String username) {
        final String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, username);
            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(username);
                    user.setPasshash(rs.getString("passhash"));
                    user.setJoined(rs.getDate("joined"));
                    user.setProfileId(rs.getInt("profile"));
                    return user;
                }
                else return null;
            }
        } catch (SQLException sqle) {
            return null;
        }
    }

    @Override
    public boolean userExists(String username) {
        return findUserByUsername(username) != null;
    }

    @Override
    public Post addPost(String content, User author) {
        content = sanitize(content);
        Post post = new Post(content, author.getUsername());
        final String sql = "INSERT INTO posts (author, content, posted) VALUES (?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstat.setString(1, post.getAuthorName());
            pstat.setString(2, post.getContent());
            pstat.setDate(3, new java.sql.Date(post.getPosted().getTime()));
            pstat.executeUpdate();
            try (ResultSet rs = pstat.getGeneratedKeys()) {
                rs.next();
                post.setId(rs.getInt(1));
                return post;
            }
        }
        catch (SQLException sqle) {
            return null;
        }       
    }

    @Override
    public Post findPostById(int id) {
        final String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, id);
            try (ResultSet rs = pstat.executeQuery()) {
                rs.next();
                Post post = new Post();
                post.setAuthorName(rs.getString("author"));
                post.setPosted(rs.getTimestamp("posted"));
                post.setContent(rs.getString("content"));
                post.setId(rs.getInt("id"));
                return post;
            }
        }
        catch (SQLException sqle) {
            return null;
        }
    }

    @Override
    public List<Post> findPostsByPage(int offset, int limit) {
        List<Post> posts = new ArrayList<>();
        final String sql = "SELECT * FROM posts ORDER BY posted DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, offset);
            pstat.setInt(2, limit);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setAuthorName(rs.getString("author"));
                    post.setPosted(rs.getTimestamp("posted"));
                    post.setContent(rs.getString("content"));
                    post.setId(rs.getInt("id"));
                    posts.add(post);
                }
            }
        }
        catch (SQLException sqle) {
            posts.clear();
        }
        return posts;
    }

    @Override
    public boolean updateProfileFor(User user, Profile changed) {
        if (!changed.isValid()) return false;
        if (changed.getBiography() != null)
            changed.setBiography(sanitize(changed.getBiography()));
        final String sql = "UPDATE profiles SET firstName=?, lastName=?, email=?, timeZone=?, biography=? WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, changed.getFirstName());
            pstat.setString(2, changed.getLastName());
            pstat.setString(3, changed.getEmail());
            pstat.setString(4, changed.getTimeZone());
            pstat.setString(5, changed.getBiography());
            pstat.setInt(6, user.getProfileId());
            pstat.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            return false;
        }
    }

    @Override
    public Comment addComment(User user, Post target, String content) {
        content = sanitize(content);
        final String sql = "INSERT INTO comments (author,target,comment,commented) VALUES (?,?,?,?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Comment comment = new Comment(user.getUsername(), target.getId(), content);
            pstat.setString(1, comment.getAuthorName());
            pstat.setInt(2, comment.getPostId());
            pstat.setString(3, comment.getComment());
            pstat.setDate(4, new java.sql.Date(comment.getCommented().getTime()));
            pstat.executeUpdate();
            try (ResultSet rs = pstat.getGeneratedKeys()) {
                rs.next();
                comment.setId(rs.getInt(1));
                return comment;
            }
        }
        catch (SQLException sqle) {
            return null;
        }
    }

    @Override
    public Profile findProfileById(int id) {
        final String sql = "SELECT * FROM profiles WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, id);
            try (ResultSet rs = pstat.executeQuery()) {
                if (!rs.next()) return null;
                Profile profile = new Profile();
                profile.setFirstName(rs.getString("firstName"));
                profile.setLastName(rs.getString("lastName"));
                profile.setEmail(rs.getString("email"));
                profile.setTimeZone(rs.getString("timeZone"));
                profile.setBiography(rs.getString("biography"));
                profile.setAvatar(rs.getBytes("avatar"));
                profile.setMime(rs.getString("mime"));
                return profile;
            }
        }
        catch (SQLException sqle) {
            return null;
        }
    }
    
    private String sanitize(String input) {
        return input
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&apos;")
                .replace("\"", "&quot;")
                .replace("%", "&#37;");                
    }       

    @Override
    public Comment findCommentById(int id) {
        final String sql = "SELECT * FROM comments WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, id);
            try (ResultSet rs = pstat.executeQuery()) {
                rs.next();
                Comment comment = new Comment();
                comment.setAuthorName(rs.getString("author"));
                comment.setComment(rs.getString("comment"));
                comment.setCommented(rs.getTimestamp("commented"));
                comment.setId(rs.getInt("id"));
                return comment;
            }
        }
        catch (SQLException sqle) {
            return null;
        }
    }

    @Override
    public List<Comment> findCommentsByPostAndPage(Post target, int offset, int limit) {
        List<Comment> comments = new ArrayList<>();
        final String sql = "SELECT * FROM comments WHERE target = ? ORDER BY commented OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, target.getId());
            pstat.setInt(2, offset);
            pstat.setInt(3, limit);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setAuthorName(rs.getString("author"));
                    comment.setComment(rs.getString("comment"));
                    comment.setCommented(rs.getTimestamp("commented"));
                    comment.setPostId(rs.getInt("target"));
                    comment.setId(rs.getInt("id"));
                    comments.add(comment);
                }
            }
        }
        catch (SQLException sqle) {}
        return comments;
    }

    @Override
    public List<Post> findPostsByAuthorAndPage(String authorName, int offset, int limit) {
        List<Post> posts = new ArrayList<>();
        final String sql = "SELECT * FROM posts WHERE author= ? ORDER BY posted DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, authorName);
            pstat.setInt(2, offset);
            pstat.setInt(3, limit);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setAuthorName(authorName);
                    post.setContent(rs.getString("content"));
                    post.setPosted(rs.getDate("posted"));
                    post.setId(rs.getInt("id"));
                    posts.add(post);
                }
            }
        }
        catch (SQLException sqle) {
            posts.clear();
        }
        return posts;
    }

    @Override
    public boolean updateAvatarFor(User user, String mime, InputStream is) {
        final String sql = "UPDATE profiles SET avatar = ?, mime = ? WHERE id = ?";
        byte[] imageData = null;
        try {
            imageData = bytesFromStream(is);
        }
        catch (IOException ioe) {
            return false;
        }
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setBytes(1, imageData);
            pstat.setString(2, mime);
            pstat.setInt(3, user.getProfileId());
            pstat.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            return false;
        }
    }
    
    private byte[] bytesFromStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1; )
                os.write(buffer, 0, len);
            os.flush();
            return os.toByteArray();
        }
    }

    @Override
    public boolean revertAvatarFor(User user) {
            final String sql = "UPDATE profiles SET mime = NULL, avatar = NULL WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setInt(1, user.getProfileId());
            pstat.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            return false;
        }
    }

    @Override
    public boolean follow(User follower, User followee) {
        return updateFollowing(follower, followee, true);
    }

    @Override
    public boolean unfollow(User follower, User followee) {
        return updateFollowing(follower, followee, false);
    }
    
    private boolean updateFollowing(User follower, User followee, boolean save) {
        final String sql = save 
                ? "INSERT INTO following VALUES (?,?)"
                : "DELETE FROM following WHERE follower = ? AND followee = ? ";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, follower.getUsername());
            pstat.setString(2, followee.getUsername());
            pstat.executeUpdate();
            return true;
        }
        catch (SQLException sqle) {
            return false;
        }        
    }

    @Override
    public List<User> findFolloweesByUser(User user) {
        return findFromFollowing(user, true);
    }

    @Override
    public List<User> findFollowersByUser(User user) {
        return findFromFollowing(user, false);
    }

    private List<User> findFromFollowing(User user, boolean direction) {
        final String sql = direction
                ? "SELECT * FROM following WHERE follower = ?"
                : "SELECT * FROM following WHERE followee = ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            pstat.setString(1, user.getUsername());
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    User other = direction 
                            ? findUserByUsername(rs.getString("followee"))
                            : findUserByUsername(rs.getString("follower"));
                    list.add(other);
                }
            }
        }
        catch (SQLException sqle) {}
        finally {
            return list;
        }
    }

}
