package edu.acc.jee.hubbub.domain;

import java.io.Serializable;
import java.util.Date;

public class Tag implements Serializable {
    private String tagName;
    private String creator;
    private Date   created;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
