package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String username;
    private String passhash;
    private Date   joined;

    public User() {
    }

    public User(String username, String passhash) {
        this.username = username;
        this.passhash = passhash;
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

    @Override
    public String toString() {
        return username;
    }
}
