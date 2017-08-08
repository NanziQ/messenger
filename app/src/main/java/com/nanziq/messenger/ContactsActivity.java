package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.nanziq.messenger.Adapters.ContactsAdapter;
import com.nanziq.messenger.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Бабуля", "http://st2-fashiony.ru/pic/clothing/pic/95436/42.jpg"));
        contacts.add(new Contact("Дедуля", "http://jumabendery.narod.ru/images/ded.jpg"));
        ContactsAdapter adapter = new ContactsAdapter(contacts, this);
        adapter.setListener(new ContactsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), "Открыть контакт " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
