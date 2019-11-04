package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Post implements Serializable {
    private static final AtomicInteger SEQ = new AtomicInteger(1);
    
    private String content;
    private User   author;
    private Date   posted;
    private Integer id;
    private List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public Post(String content, User author) {
        this(content, author, new Date());
    }

    public Post(String content, User author, Date posted) {
        this.content = content;
        this.author = author;
        this.posted = posted;
        this.id = SEQ.incrementAndGet();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post[id:" + id + ", content length:" + content.length() + 
                ", author=" + author + ", posted=" + posted + '}';
    }
}
