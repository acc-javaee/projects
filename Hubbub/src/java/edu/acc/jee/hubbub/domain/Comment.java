package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Comment implements Serializable {
    private static final AtomicInteger SEQ = new AtomicInteger();
    
    private String authorName;
    private int postId;
    private String comment;
    private Date commented = new Date();
    private Integer id = SEQ.incrementAndGet();
    
    public Comment() {}
    
    public Comment(String authorName, int postId, String comment) {
        this.authorName = authorName;
        this.postId = postId;
        this.comment = comment;
        this.commented = new Date();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getPostId() {
        return postId;
    }
    
    public void setPostId(int postId) {
        this.postId = postId;
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
        return "Comment{" + "authorName=" + authorName + ", target=" + postId +
                ", comment=" + comment + ", id=" + id + '}';
    }
    
    
}
