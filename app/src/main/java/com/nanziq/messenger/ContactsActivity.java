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
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Contact, ContactsAdapter.ViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
                                .load(model.getImage())
                                .into(viewHolder.binding.imageView);
                    }

                    @Override
                    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ContactsAdapter.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ContactsAdapter.ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

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
}
