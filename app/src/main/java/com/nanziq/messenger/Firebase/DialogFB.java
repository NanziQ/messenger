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
    private Map<String, Object> dialogMap;

    private DialogFB (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dialogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialogMap =  (Map<String, Object>) dataSnapshot.getValue();
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
    public List<Dialog> getContactDialogList(String id){
        List<Dialog> contactDialogList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dialogMap.entrySet()){
            Map dialog = (Map) entry.getValue();
            Map<String, Object> idList = (Map<String, Object>) dialog.get("contacts");
            for (Map.Entry<String, Object> idContact : idList.entrySet()){
                Map value = (Map) idContact.getValue();
                String idValue = value.get("id").toString();
                if(idValue.equals(id)){
                    contactDialogList.add(convertMapToDialog(dialog));
                    break;
                }
            }
        }
        return contactDialogList;
    }

    private Dialog convertMapToDialog(Map map){
        Dialog dialog = new Dialog();
        dialog.setName((String) map.get("name"));
        dialog.setImage((String) map.get("image"));
        dialog.setContacts((List<Long>) convertMapToList(map, "contacts"));
        dialog.setDate((Date) map.get("date"));
        dialog.setMessages((List<Message>) convertMapToMessageList(map));
        dialog.setText((String) map.get("text"));
        return dialog;
    }

    private Message convertMapToMessage(Map map){
        Message message = new Message();
        message.setText((String) map.get("text"));
        message.setName((String) map.get("name"));
        return message;
    }

    private List convertMapToMessageList(Map map){
        List list = new ArrayList();
        Map<String, Object> convertable = (Map<String, Object>) map.get("messages");
        for (Map.Entry<String, Object> item : convertable.entrySet()){
            Map value = (Map) item.getValue();
            list.add(convertMapToMessage(value));
        }
        return list;
    }

    private List convertMapToList(Map map, String key){
        List list = new ArrayList();
        Map<String, Object> convertable = (Map<String, Object>) map.get(key);
        for (Map.Entry<String, Object> item : convertable.entrySet()){
            Map value = (Map) item.getValue();
            list.add(value);
        }
        return list;
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
