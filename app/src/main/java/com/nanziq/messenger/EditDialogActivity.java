package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.nanziq.messenger.Adapters.EditDialogMembersAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class EditDialogActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSettings;
    private RecyclerView recyclerViewMembers;
    private DialogFB dialogFB;
    private String dialogId;
    private ContactFB contactFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialogFB = DialogFB.getInstance();
        contactFB = ContactFB.getInstance();
        dialogId = getIntent().getStringExtra("dialogId");

        recyclerViewMembers = (RecyclerView) findViewById(R.id.recycler_view_members);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMembers.setHasFixedSize(true);
        List<String> contactStringList = dialogFB.getDialog(dialogId).getContacts();
        List<Contact> contactList = new ArrayList<>();
        for (String str: contactStringList){
            contactList.add(contactFB.getContactFromUid(str));
        }
        EditDialogMembersAdapter adapter = new EditDialogMembersAdapter(contactList, this);
        recyclerViewMembers.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
