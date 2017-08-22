package com.nanziq.messenger.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class Dialog {
    public String name;
    public String image;
    public String text;
    public List<Message> messages;
    public List<String> contacts;
    public boolean solo;

    public Dialog(){

    }

    public Dialog(String name, String image, String text, List<Message> messages, List<String> contacts, boolean solo){
        this.name = name;
        this.image = image;
        this.text = text;
        this.messages = messages;
        this.contacts = contacts;
        this.solo = solo;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public boolean getSolo() {
        return solo;
    }

    public void setSolo(boolean solo){
        this.solo = solo;
    }

}
