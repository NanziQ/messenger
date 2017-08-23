package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nanziq.messenger.Model.Message;

/**
 * Created by Konstantin on 23.08.2017.
 */

public class MessageFB {

    private static MessageFB instance;
    private DatabaseReference databaseReference;

    private MessageFB (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static MessageFB getInstance(){
        if (null == instance){
            instance = new MessageFB();
        }
        return instance;
    }

    public void sendMessage(String dialogId, String name, String text){
        Message message = new Message(name, text);
        databaseReference.child("dialogs").child(dialogId).child("messages").push().setValue(message);
    }
}
