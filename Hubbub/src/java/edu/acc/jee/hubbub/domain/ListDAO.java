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

    @Override
    public synchronized User addUser(UserDTO bean) {
        if (!bean.isValid())
            throw new IllegalArgumentException("Bean does not validate");
        if (this.userExists(bean.getUsername()))
            throw new IllegalArgumentException("Username " + bean.getUsername() + " is unavailable");
        String hash = HashTool.hash(bean.getPassword());
        User user = new User(bean.getUsername(), hash);
        users.add(user);
        return user;
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
        content = content
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&apos;")
                .replace("\"", "&quot;")
                .replace("%", "&#37;");
        Post post = new Post(content, author);
        author.getPosts().add(post);
        posts.add(post);
        return post;
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
}
