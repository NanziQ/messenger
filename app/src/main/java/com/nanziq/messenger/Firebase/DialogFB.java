package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Konstantin on 18.08.2017.
 */

public class DialogFB {
    private static DialogFB instance;
    private DatabaseReference databaseReference;
    private List<Dialog> dialogList;

    private DialogFB (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dialogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialogList = (List<Dialog>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static DialogFB getInstance(){
        if (null == instance){
            instance = new DialogFB();
        }
        return instance;
    }

    private void createNewDialog(String name, String image, Date date, String text, List<Message> messages, List<Long> contacts, boolean solo){
        Dialog dialog = new Dialog(name, image, date, text, messages, contacts, solo);
        databaseReference.child("dialogs").push().setValue(dialog);
    }
    public List<Dialog> getDialogList(String id){
        List<Dialog> dialogList = new ArrayList<>();

        return dialogList;
    }
//    private List<Dialog> collectAllDialogs(Map<Dialog,Object> users){
//        ArrayList<Dialog> phoneNumbers = new ArrayList<>();
//
//        for (Map.Entry<Dialog, Object> entry : users.entrySet()){
//            Map singleUser = (Map) entry.getValue();
//            phoneNumbers.add((Dialog) singleUser.get("phone"));
//        }
//        return phoneNumbers;
//    }
}
