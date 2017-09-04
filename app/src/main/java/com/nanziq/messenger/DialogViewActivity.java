package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nanziq.messenger.Adapters.ContactsAdapter;
import com.nanziq.messenger.Adapters.MessageViewHolder;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.MessageFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Message;

public class DialogViewActivity extends AppCompatActivity {
    private String dialogId = "-666";
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private MessageFB messageFB;
    private ContactFB contactFB;
    private EditText enterText;
    private Button buttonSend;
    private StorageReference storageReference;
    private Contact databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_view);
        dialogId = getIntent().getStringExtra("dialogId");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        enterText = (EditText) findViewById(R.id.enterText);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        messageFB = MessageFB.getInstance();
        contactFB = ContactFB.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference  = FirebaseStorage.getInstance().getReference();
        databaseUser = contactFB.getContactFromUid(firebaseUser.getUid());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class,
                        R.layout.cardview_message,
                        MessageViewHolder.class,
                        databaseReference.child("dialogs").child(dialogId).child("messages")) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                        viewHolder.binding.setMessage(model);
                        if (model.getUid().equals(firebaseUser.getUid())){
                            viewHolder.binding.messageCardView.setBackground(getResources().getDrawable(R.color.colorYourselfUser));
                        }
                        Glide
                                .with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(storageReference.child("images/" + databaseUser.getImage()))
                                .into(viewHolder.binding.messageImage);
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

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageFB.sendMessage(dialogId, firebaseUser.getUid(), enterText.getText().toString());
                enterText.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
