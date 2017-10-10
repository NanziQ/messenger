package com.nanziq.messenger.Model;

/**
 * Created by Konstantin on 03.10.2017.
 */

public class ContactsInDialog {
    private String uid;
    private long countUnreadMessage;

    public ContactsInDialog() {
        this.countUnreadMessage = 0;
    }

    public ContactsInDialog(String uid, long countUnreadMessage) {
        this.uid = uid;
        this.countUnreadMessage = countUnreadMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getCountUnreadMessage() {
        return countUnreadMessage;
    }

    public void setCountUnreadMessage(long countUnreadMessage) {
        this.countUnreadMessage = countUnreadMessage;
    }
}
