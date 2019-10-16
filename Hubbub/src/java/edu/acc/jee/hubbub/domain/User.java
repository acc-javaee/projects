package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    public static final String USERNAME_PATTERN = "^\\w{6,20}$";
    
    private String username;
    private String passhash;
    private Date   joined;
    private List<Post> posts = new ArrayList<>();
    
    public User() {
    }

    public User(String username, String passhash) {
        this.username = username;
        this.passhash = passhash;
        this.joined = new Date();
    }

    public User(String username, String passhash, Date joined) {
        this.username = username;
        this.passhash = passhash;
        this.joined = joined;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return username;
    }
}
