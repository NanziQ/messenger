package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Konstantin on 21.08.2017.
 */

public class ContactFB {
    private static ContactFB instance;
    private DatabaseReference databaseReference;
    private Map<String, Object> contactMap;

    private ContactFB (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactMap =  (Map<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ContactFB getInstance(){
        if (null == instance){
            instance = new ContactFB();
        }
        return instance;
    }

    public String getContactUid(int position){
        for (Map.Entry<String, Object> entry: contactMap.entrySet()){
            int i = 0;
            if (i == position){
                Map contact = (Map) entry.getValue();
                return contact.get("uid").toString();
            }
        }
        return null;
    }
}
