package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Comment implements Serializable {
    private static final AtomicInteger SEQ = new AtomicInteger(1);
    
    private User author;
    private Post target;
    private String comment;
    private Date commented;
    private Integer id;
    
    public Comment() {}
    
    public Comment(User author, Post target, String comment) {
        this.author = author;
        this.target = target;
        this.comment = comment;
        this.commented = new Date();
        this.id = SEQ.incrementAndGet();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getTarget() {
        return target;
    }

    public void setTarget(Post target) {
        this.target = target;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getCommented() {
        return commented;
    }
    
    public void setCommented(Date commented) {
        this.commented = commented;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Comment{" + "author=" + author + ", target=" + target +
                ", comment=" + comment + ", id=" + id + '}';
    }
    
    
}
