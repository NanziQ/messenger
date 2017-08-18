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
    public Date date;
    public String text;
    public List<Message> messages;
    public List<Long> contacts;
    public boolean solo;

    public Dialog(){

    }

    public Dialog(String name, String image, Date date, String text, List<Message> messages, List<Long> contacts, boolean solo){
        this.name = name;
        this.image = image;
        this.date = date;
        this.text = text;
        this.messages = messages;
        this.contacts = contacts;
        this.solo = solo;
    }

    public String getDateFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM\nHH.mm");
        String dateString = formatter.format(date);
        return dateString;

    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public List<Long> getContacts() {
        return contacts;
    }

    public void setContacts(List<Long> contacts) {
        this.contacts = contacts;
    }

}
