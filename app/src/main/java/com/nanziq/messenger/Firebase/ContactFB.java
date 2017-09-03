package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.Model.Contact;

import java.util.List;
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
        databaseReference.child("contacts").addValueEventListener(new ValueEventListener() {
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

    public void updateContact(Contact contact){
        databaseReference.child("contacts").child(getKeyFromUid(contact.getUid())).setValue(contact);

    }

    public String getKeyFromUid(String uid){
        for (Map.Entry<String, Object> entry: contactMap.entrySet()){
            Map contact = (Map) entry.getValue();
            if (contact.get("uid").toString().equals(uid)){
                return entry.getKey();
            }
        }
        return null;
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
                String name = contact.get("name").toString();
                if (name != null) {
                    return name;
                }else {
                    return null;
                }
            }
        }
        return null;
    }

    public String getContactNameFromUid(String uid){
        for (Map.Entry<String, Object> entry: contactMap.entrySet()){
            Map contact = (Map) entry.getValue();
            if (contact.get("uid").equals(uid)){
                String name = contact.get("name").toString();
                if (name != null) {
                    return name;
                }else {
                    return null;
                }
            }
        }
        return null;
    }

    public Contact getContactFromUid(String uid){
        Contact contact;
        if (contactMap != null) {
            for (Map.Entry<String, Object> entry : contactMap.entrySet()) {
                Map map = (Map) entry.getValue();
                if (map.get("uid").equals(uid)) {
                    contact = convertMapToContact(map);
                    if (contact != null) {
                        return contact;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public Contact convertMapToContact(Map map){
        Contact contact = new Contact();
        contact.setUid((String) map.get("uid"));
        contact.setName((String) map.get("name"));
        contact.setPhone((String) map.get("phone"));
        contact.setImage((String) map.get("image"));
        return contact;
    }
}
