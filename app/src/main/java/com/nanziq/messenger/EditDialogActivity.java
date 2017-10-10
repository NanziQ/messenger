package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.nanziq.messenger.Adapters.EditDialogMembersAdapter;
import com.nanziq.messenger.Adapters.EditDialogSettingsAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.ContactsInDialog;

import java.util.ArrayList;
import java.util.Arrays;
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

        recyclerViewSettings = (RecyclerView) findViewById(R.id.recycler_view_settings);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSettings.setHasFixedSize(true);
        List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.editDialogSettings_strings));
        EditDialogSettingsAdapter adapterSettings = new EditDialogSettingsAdapter(stringList);
        adapterSettings.setOnClickListener(new EditDialogSettingsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), "123123123", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewSettings.setAdapter(adapterSettings);

        recyclerViewMembers = (RecyclerView) findViewById(R.id.recycler_view_members);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMembers.setHasFixedSize(true);
        List<ContactsInDialog> contactStringList = dialogFB.getDialog(dialogId).getContacts();
        List<Contact> contactList = new ArrayList<>();
        for (ContactsInDialog str: contactStringList){
            contactList.add(contactFB.getContactFromUid(str.getUid()));
        }
        EditDialogMembersAdapter adapterMembers = new EditDialogMembersAdapter(contactList, this);
        recyclerViewMembers.setAdapter(adapterMembers);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
