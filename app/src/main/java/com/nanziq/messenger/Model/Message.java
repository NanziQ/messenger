package com.nanziq.messenger.Model;

import com.nanziq.messenger.Firebase.ContactFB;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Konstantin on 17.08.2017.
 */

public class Message {
    private ContactFB contactFB = ContactFB.getInstance();

    public String uid;
    public String text;
    public long date;

    public Message(String uid, String text, long date) {
        this.uid = uid;
        this.text = text;
        this.date = date;
    }

    public Message() {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static final Comparator<Message> SORT_BY_DATE = new Comparator<Message>() {
        @Override
        public int compare(Message message, Message message1) {
            return (int) (message.getDate() - message1.getDate());
        }
    };

    public String getNameFormatted() {
        if (this.uid == null) {
            return null;
        } else {
            return contactFB.getContactNameFromUid(this.uid);
        }
    }

    public String getDateFormatted() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm");
        String dateString = simpleDateFormat.format(new Date(date));
        return dateString;
    }
}
