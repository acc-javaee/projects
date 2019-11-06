package edu.acc.jee.emailr;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {
    private String address;
    private Date added;
    private Boolean subscribed;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }
}
