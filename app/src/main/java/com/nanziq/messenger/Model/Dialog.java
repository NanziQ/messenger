package com.nanziq.messenger.Model;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class Dialog {
    public String id;
    public String name;
    public String image;
    public String text;
    public List<Message> messages;
    public List<String> contacts;
    public boolean solo;
    private DialogFB dialogFB;
    private String currentUid;
    private Message message;


    public Dialog() {
    }

    public Dialog(String name, String image, String text, List<Message> messages, List<String> contacts, boolean solo) {
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

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDialogNameFormat() {
        if (!this.solo) {
            return this.name;
        } else {
            ContactFB contactFB = ContactFB.getInstance();
            if (this.contacts.size() == 1) {
                return contactFB.getContactNameFromUid(this.contacts.get(0));
            } else {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (String contact : this.contacts) {
                    if (!contact.equals(uid)) {
                        return contactFB.getContactNameFromUid(contact);
                    }
                }
            }
        }
        return null;
    }

    public String getLastMessageFormatted() {
        dialogFB = DialogFB.getInstance();
        message = dialogFB.getLastMessageFromDialogId(this.id);
        String retMes = String.format("%s: %s", message.getNameFormatted(), message.getText());
        return retMes;
    }

    public String getDateFromLastMessageFormatted() {
        dialogFB = DialogFB.getInstance();
        message = dialogFB.getLastMessageFromDialogId(this.id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm \ndd.MM ");
        String dateString = simpleDateFormat.format(new Date(message.getDate()));
        return dateString;
    }
}
