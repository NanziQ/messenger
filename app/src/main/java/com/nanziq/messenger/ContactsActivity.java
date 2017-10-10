package com.nanziq.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
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
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.ContactsInDialog;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Contact, ContactsAdapter.ViewHolder> firebaseRecyclerAdapter;
    private DialogFB dialogFB;
    private ContactFB contactFB;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        storageReference = FirebaseStorage.getInstance().getReference();
        dialogFB = DialogFB.getInstance();
        contactFB = ContactFB.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Contact, ContactsAdapter.ViewHolder>(Contact.class,
                        R.layout.cardview_contacts,
                        ContactsAdapter.ViewHolder.class,
                        databaseReference.child("contacts")) {
                    @Override
                    protected void populateViewHolder(ContactsAdapter.ViewHolder viewHolder, Contact model, int position) {
                        viewHolder.binding.setContact(model);
                        Glide
                                .with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(storageReference.child("images/" + model.getImage()))
                                .placeholder(R.drawable.ic_account_black_48dp)
                                .into(viewHolder.binding.imageView);

                    }

                    @Override
                    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ContactsAdapter.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ContactsAdapter.ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Dialog> currentUserSoloDialogList = dialogFB.getSoloContactDialogList(firebaseUser.getUid());
//                                List<Dialog> friendSoloDialogList = dialogFB.getSoloContactDialogList(contactFB.getContactUid(position));

                                String friendUid = contactFB.getContactUid(firebaseRecyclerAdapter.getRef(position).getKey());
                                boolean checkDialog = false;
                                String key = "";

                                String userUid = firebaseUser.getUid();

                                if (!userUid.equals(friendUid)) {
                                    for (Dialog item : currentUserSoloDialogList) {
                                        List<ContactsInDialog> contacts = item.getContacts();
                                        for (ContactsInDialog id : contacts) {
                                            if (friendUid.equals(id.getUid())) {
                                                checkDialog = true;
                                                key = item.getId();
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    for (Dialog item : currentUserSoloDialogList) {
                                        List<ContactsInDialog> contacts = item.getContacts();
                                        if (contacts.size() == 1 && contacts.get(0).getUid().equals(userUid)){
                                            checkDialog = true;
                                            key = item.getId();
                                            break;
                                        }
                                    }
                                }
                                if (checkDialog) {
                                    Intent intent = new Intent(getApplicationContext(), DialogViewActivity.class);
                                    intent.putExtra("dialogId", key);
                                    startActivity(intent);
                                }else {
                                    List<ContactsInDialog> contactsId = new ArrayList<>();
                                    ContactsInDialog contactsInDialog = new ContactsInDialog();
                                    contactsInDialog.setUid(userUid);
                                    contactsId.add(contactsInDialog);
                                    if (!userUid.equals(friendUid)) {
                                        ContactsInDialog contactsInDialog1 = new ContactsInDialog();
                                        contactsInDialog1.setUid(friendUid);
                                        contactsId.add(contactsInDialog1);
                                    }
                                    key = dialogFB.createNewDialog("Dialog" + position, contactsId, true);
                                    Intent intent = new Intent(getApplicationContext(), DialogViewActivity.class);
                                    intent.putExtra("dialogId", key);
                                    startActivity(intent);
                                }
                            }
                        });
                        return viewHolder;
                    }
                };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
//        List<Contact> contacts = new ArrayList<>();
//        contacts.add(new Contact("Бабуля", "+796265444545", "http://st2-fashiony.ru/pic/clothing/pic/95436/42.jpg", null));
//        contacts.add(new Contact("Дедуля", "+796265447896", "http://jumabendery.narod.ru/images/ded.jpg", null));
//        ContactsAdapter adapter = new ContactsAdapter(contacts, this);
//        adapter.setListener(new ContactsAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getApplicationContext(), "Открыть контакт " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
