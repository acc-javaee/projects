package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Post implements Serializable {
    private static final AtomicInteger SEQ = new AtomicInteger();
    
    private String content;
    private String authorName;
    private Date   posted;
    private Integer id = SEQ.incrementAndGet();

    public Post() {
    }

    public Post(String content, String authorName) {
        this(content, authorName, new Date());
    }

    public Post(String content, String authorName, Date posted) {
        this.content = content;
        this.authorName = authorName;
        this.posted = posted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
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
                ", authorName=" + authorName + ", posted=" + posted + '}';
    }
}
