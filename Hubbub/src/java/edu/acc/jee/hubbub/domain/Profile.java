package edu.acc.jee.hubbub.domain;

import java.io.Serializable;

public class Profile implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String timeZone;
    private String biography;
    private byte[] avatar;
    private String mime;
    private Integer id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public boolean isValid() {
        final String safe = "[^<>'\"%]+";
        return (
            (firstName == null || firstName.length() <= 20 || firstName.matches(safe))
                &&
            (lastName == null || lastName.length() <= 30) || lastName.matches(safe))
                &&
            (email == null || email.length() <= 100 || email.matches(safe + "@" + safe + "\\." +safe))
                &&
            (timeZone == null || timeZone.length() <= 50 || timeZone.matches("[\\w/]+")
                &&
            (biography == null || biography.length() <= 512));    
    }
    
    @Override
    public String toString() {
        return String.format("Profile[id=%d]", id == null ? 0 : id);
    }
}
