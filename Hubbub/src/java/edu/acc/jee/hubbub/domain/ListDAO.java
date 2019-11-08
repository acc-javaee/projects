package edu.acc.jee.hubbub.domain;

import edu.acc.jee.hashtool.HashTool;
import edu.acc.jee.hubbub.UserDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListDAO implements DataService {
    private List<User> users = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<Profile> profiles = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    @Override
    public synchronized User addUser(UserDTO bean) {
        if (!bean.isValid())
            throw new IllegalArgumentException("Bean does not validate");
        if (this.userExists(bean.getUsername()))
            throw new IllegalArgumentException("Username " + bean.getUsername() + " is unavailable");
        String hash = HashTool.hash(bean.getPassword());
        Profile profile = addProfile();
        User user = new User(bean.getUsername(), hash, profile.getId());
        users.add(user);
        return user;
    }
    
    @Override
    public Profile addProfile() {
        Profile profile = new Profile();
        profiles.add(profile);
        return profile;
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
        Optional<User> userOpt = users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
        if (userOpt.isPresent()) return userOpt.get();
        else return null;
    }

    @Override
    public boolean userExists(String username) {
        return this.findUserByUsername(username) != null;
    }

    @Override
    public synchronized Post addPost(String content, User author) {
        content = sanitize(content);
        Post post = new Post(content, author.getUsername());
        posts.add(post);
        return post;
    }
    
    @Override
    public Post findPostById(int id) {
        for (Post p : posts)
            if (p.getId() == id)
                return p;
        return null;
    }

    @Override
    public List<Post> findPostsByPage(int offset, int limit) {
        return posts
            .stream()
            .sorted((a,b) -> b.getPosted().compareTo(a.getPosted()))
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateProfileFor(User user, Profile changed) {
        if (!changed.isValid()) return false;
        Profile profile = findProfileById(user.getProfileId());
        if (profile == null) return false;
        profile.setFirstName(changed.getFirstName());
        profile.setLastName(changed.getLastName());
        profile.setEmail(changed.getEmail());
        profile.setTimeZone(changed.getTimeZone());
        if (changed.getBiography() != null)
            profile.setBiography(sanitize(changed.getBiography()));
        return true;  
    }
    
    @Override
    public Profile findProfileById(int id) {
        for (Profile profile : profiles)
            if (profile.getId() == id)
                return profile;
        return null;
    }
    
    @Override
    public Comment addComment(User author, Post target, String content) {
        content = sanitize(content);    
        Comment comment = new Comment(author.getUsername(), target.getId(), content);
        comments.add(comment);
        return comment;
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
        for (Comment comment : comments)
            if (comment.getId() == id)
                return comment;
        return null;
    }

    @Override
    public List<Comment> findCommentsByPostAndPage(Post target, int offset, int limit) {
        return comments
            .stream()
            .filter((c) -> c.getPostId() == target.getId())
            .sorted((a,b) -> a.getCommented().compareTo(b.getCommented()))
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public List<Post> findPostsByAuthorAndPage(String authorName, int offset, int limit) {
        return posts
            .stream()
            .filter(p -> p.getAuthorName().equals(authorName))
            .sorted((a,b) -> b.getPosted().compareTo(a.getPosted()))
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
    }
}
