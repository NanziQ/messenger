package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nanziq.messenger.Adapters.ContactsAdapter;
import com.nanziq.messenger.Adapters.MessageViewHolder;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Message;

public class DialogViewActivity extends AppCompatActivity {
    private String dialogId = "-123";
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_view);
        dialogId = getIntent().getStringExtra("dialogId");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class,
                        R.layout.cardview_message,
                        MessageViewHolder.class,
                        databaseReference.child("dialogs").child(dialogId).child("messages")) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                        viewHolder.binding.setMessage(model);
                    }

                    @Override
                    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        MessageViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new MessageViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "Ты нажал на сообщение", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return viewHolder;
                    }
                };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
