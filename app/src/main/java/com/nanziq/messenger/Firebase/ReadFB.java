package com.nanziq.messenger.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.Interfaces.OnGetDataListener;

/**
 * Created by Konstantin on 23.08.2017.
 */

public class ReadFB {
    private static ReadFB instance;
    private DatabaseReference databaseReference;

    public ReadFB(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static ReadFB getInstance(){
        if (instance == null){
            instance = new ReadFB();
        }
        return instance;
    }

    public void readDataFromFB(String child, final OnGetDataListener listener){
        listener.onStart();
        databaseReference.child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
}
