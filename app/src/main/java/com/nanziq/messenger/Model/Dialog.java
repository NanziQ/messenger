package com.nanziq.messenger.Model;

import android.content.Intent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class Dialog {
    private String id;
    private String name;
    private String image;
    private String text;
    private List<Message> messages;
    private List<ContactsInDialog> contacts;
    private boolean solo;

    private DialogFB dialogFB;
    private Message message;


    public Dialog() {
    }

    public Dialog(String name, String image, String text, List<Message> messages, List<ContactsInDialog> contacts, boolean solo) {
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

    public List<ContactsInDialog> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsInDialog> contacts) {
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
            if (this.contacts == null) {
                return null;
            } else {
                if (this.contacts.size() == 1) {
                    return contactFB.getContactNameFromUid(this.contacts.get(0).getUid());
                } else {

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (ContactsInDialog contact : this.contacts) {
                        if (!contact.getUid().equals(uid)) {
                            return contactFB.getContactNameFromUid(contact.getUid());
                        }
                    }
                }
            }

        }
        return null;
    }

    public String getLastMessageFormatted() {
        dialogFB = DialogFB.getInstance();
        message = dialogFB.getLastMessageFromDialogId(this.id);
        if (message != null) {
            String retMes = String.format("%s: %s", message.getNameFormatted(), message.getText());
            return retMes;
        } else {
            return null;
        }
    }

    public String getDateFromLastMessageFormatted() {
        dialogFB = DialogFB.getInstance();
        message = dialogFB.getLastMessageFromDialogId(this.id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm \ndd.MM ");
        if (message != null) {
            String dateString = simpleDateFormat.format(new Date(message.getDate()));
            return dateString;
        } else {
            return null;
        }
    }

    public long getDateFromLastMessage() {
        dialogFB = DialogFB.getInstance();
        message = dialogFB.getLastMessageFromDialogId(this.id);
        return message.getDate();

    }

    public static final Comparator<Dialog> SORT_BY_DATE = new Comparator<Dialog>() {
        @Override
        public int compare(Dialog dialog, Dialog dialog1) {

            return (int) (dialog1.getDateFromLastMessage() - dialog.getDateFromLastMessage());
        }
    };
}
