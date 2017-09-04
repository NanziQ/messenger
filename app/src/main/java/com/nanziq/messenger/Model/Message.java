package com.nanziq.messenger.Model;

import com.nanziq.messenger.Firebase.ContactFB;

/**
 * Created by Konstantin on 17.08.2017.
 */

public class Message {
    private ContactFB contactFB = ContactFB.getInstance();

    public String uid;
    public String text;

    public Message(){}

    public Message(String uid, String text) {
        this.uid = uid;
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameFormat(){
        if (this.uid == null){
            return null;
        } else {
            return contactFB.getContactNameFromUid(this.uid);
        }
    }
}
