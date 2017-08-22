package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Konstantin on 21.08.2017.
 */

public class ContactFB {
    private static ContactFB instance;
    private DatabaseReference databaseReference;
    private Map<String, Object> contactMap;
    private TreeMap<String, Object> sortContactMap = new TreeMap<>();

    private ContactFB (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactMap =  (Map<String, Object>) dataSnapshot.getValue();
                sortContactMap.putAll(contactMap);
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

    public String getContactUid(String key){
        for (Map.Entry<String, Object> entry: contactMap.entrySet()){
            if (entry.getKey().equals(key)){
                Map contact = (Map) entry.getValue();
                String uid = contact.get("uid").toString();
                if (uid != null) {
                    return uid;
                }else {
                    return null;
                }
            }
        }
        return null;
    }

    public String getContactName(String key){
        for (Map.Entry<String, Object> entry: contactMap.entrySet()){
            if (entry.getKey().equals(key)){
                Map contact = (Map) entry.getValue();
                String uid = contact.get("name").toString();
                if (uid != null) {
                    return uid;
                }else {
                    return null;
                }
            }
        }
        return null;
    }
}
