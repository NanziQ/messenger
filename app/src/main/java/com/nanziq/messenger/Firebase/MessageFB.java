package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nanziq.messenger.Model.ContactsInDialog;
import com.nanziq.messenger.Model.Message;

import java.util.Date;
import java.util.List;

/**
 * Created by Konstantin on 23.08.2017.
 */

public class MessageFB {

    private static MessageFB instance;
    private DatabaseReference databaseReference;
    private DialogFB dialogFB = DialogFB.getInstance();

    private MessageFB() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static MessageFB getInstance() {
        if (null == instance) {
            instance = new MessageFB();
        }
        return instance;
    }

    public void sendMessage(String dialogId, String uid, String text) {
        Message message = new Message(uid, text, new Date().getTime());
        databaseReference.child("dialogs").child(dialogId).child("messages").push().setValue(message);
        List<ContactsInDialog> contactsInDialog = dialogFB.getDialog(dialogId).getContacts();
        databaseReference.child("dialogs").child(dialogId).child("contacts").removeValue();
        for (ContactsInDialog contact : contactsInDialog) {
            if (!contact.getUid().equals(uid)){
                contact.setCountUnreadMessage(contact.getCountUnreadMessage() + 1);
            }
            databaseReference.child("dialogs").child(dialogId).child("contacts").push().setValue(contact);
        }

    }
}
