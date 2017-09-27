package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.Interfaces.OnGetDataListener;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.Message;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private List<Dialog> dialogList;
    private String dialogId;

    private DialogFB() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dialogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialogList = getDialogList();
                dialogMap = (Map<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static DialogFB getInstance() {
        if (null == instance) {
            instance = new DialogFB();
        }
        return instance;
    }

    public String createNewDialog(String name, String image, String text, List<Message> messages, List<String> contacts, boolean solo) {
        Dialog dialog = new Dialog(name, image, text, messages, contacts, solo);
        String key = databaseReference.push().getKey();
        databaseReference.child("dialogs").child(key).setValue(dialog);
        return key;
    }

    public String getIdDialogFromNewMesasge() {
        return dialogId;
    }

    public Message getContactNewMessageFromUid(String uid) {
        List<Dialog> contactDialogList = getContactDialogList(uid);
        List<Dialog> contactDialogListOld = getContactDialogListFromList(uid);
        if (contactDialogList == null || contactDialogListOld == null || contactDialogList == contactDialogListOld) {
            return null;
        } else {
            for (int i = 0; i < contactDialogList.size(); i++) {
                if (contactDialogList.get(i) != contactDialogListOld.get(i)) {
                    List<Message> messages = contactDialogList.get(i).getMessages();
                    if (messages != null) {
                        Collections.sort(messages, Message.SORT_BY_DATE);
                    }
                    List<Message> messagesOld = contactDialogListOld.get(i).getMessages();
                    if (messagesOld != null) {
                        Collections.sort(messagesOld, Message.SORT_BY_DATE);
                    }
                    if (messages == null || messagesOld == null || messages.size() == messagesOld.size()) {
                        continue;
                    } else {
                        dialogId = contactDialogList.get(i).getId();
                        return messages.get(messages.size() - 1);
                    }
                }
            }
        }
        return null;
    }

    public List<Dialog> getContactDialogListFromList(String uid) {
        List<Dialog> contactDialogList = new ArrayList<>();
        if (dialogList != null) {
            for (Dialog dialog : dialogList) {
                for (String contact : dialog.getContacts()) {
                    if (contact.equals(uid)) {
                        contactDialogList.add(dialog);
                        break;
                    }
                }
            }
        } else {
            return null;
        }
        return contactDialogList;
    }

    public List<Dialog> getContactDialogList(final String uid) {
        final List<Dialog> contactDialogList = new ArrayList<>();
        if (dialogMap != null) {
            for (Map.Entry<String, Object> entry : dialogMap.entrySet()) {
                Map dialog = (Map) entry.getValue();
                List<String> idList = (List<String>) dialog.get("contacts");
                if (idList != null) {
                    for (String idContact : idList) {
                        if (idContact.equals(uid)) {
                            contactDialogList.add(convertMapToDialog(dialog, entry.getKey()));
                            break;
                        }
                    }
                }
            }
        } else {
            return null;
        }
        return contactDialogList;
    }

    public List<Dialog> getSoloContactDialogList(String id) {
        List<Dialog> dialogList = getContactDialogList(id);
        List<Dialog> newDialogList = new ArrayList<>();
        for (Dialog item : dialogList) {
            if (item.getSolo()) {
                newDialogList.add(item);
            }
        }
        return newDialogList;
    }

    public List<Dialog> getDialogList() {
        List<Dialog> dialogList = new ArrayList<>();
        if (dialogMap != null) {
            for (Map.Entry<String, Object> entry : dialogMap.entrySet()) {
                Map dialog = (Map) entry.getValue();
                dialogList.add(convertMapToDialog(dialog, entry.getKey()));
            }
        } else {
            return null;
        }
        return dialogList;
    }

    private Dialog convertMapToDialog(Map map, String key) {
        Dialog dialog = new Dialog();
        dialog.setId(key);
        dialog.setName((String) map.get("name"));
        dialog.setImage((String) map.get("image"));
        dialog.setContacts((List<String>) map.get("contacts"));
        dialog.setMessages((List<Message>) convertMapToMessageList(map));
        dialog.setText((String) map.get("text"));
        dialog.setSolo((boolean) map.get("solo"));
        return dialog;
    }

    private Message convertMapToMessage(Map map) {
        Message message = new Message();
        message.setText((String) map.get("text"));
        message.setUid((String) map.get("uid"));
        message.setDate((long) map.get("date"));
        return message;
    }

    private List convertMapToMessageList(Map map) {
        List list = new ArrayList();
        Map<String, Object> convertable = (Map<String, Object>) map.get("messages");
        if (convertable != null) {
            for (Map.Entry<String, Object> item : convertable.entrySet()) {
                Map value = (Map) item.getValue();
                list.add(convertMapToMessage(value));
            }
            return list;
        } else {
            return null;
        }
    }

    private List convertMapToList(Map map, String key) {
        List list = new ArrayList();
        Map<String, Object> convertable = (Map<String, Object>) map.get(key);
        for (Map.Entry<String, Object> item : convertable.entrySet()) {
            Map value = (Map) item.getValue();
            list.add(value);
        }
        return list;
    }

    public Dialog getDialog(String id) {
        Dialog dialog = null;
        if (dialogMap != null) {
            for (Map.Entry<String, Object> entry : dialogMap.entrySet()) {
                Map dialogMap = (Map) entry.getValue();
                if (entry.getKey().equals(id)) {
                    dialog = convertMapToDialog(dialogMap, entry.getKey());
                    break;
                }
            }
        } else {
            return null;
        }

        return dialog;
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
