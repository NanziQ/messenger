package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nanziq.messenger.Adapters.CreateDialogAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Dialog;

import java.util.ArrayList;
import java.util.List;

public class CreateDialogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactFB contactFB;
    private List<Contact> contactList;
    private CreateDialogAdapter adapter;
    private DialogFB dialogFB;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactFB = ContactFB.getInstance();
        contactList = contactFB.getContactList();
        int i = 0;
        for (Contact contact: contactList) {
            if (contact.getUid().equals(firebaseUser.getUid())) {
                break;
            }
            i++;
        }
        contactList.remove(i);
        dialogFB = DialogFB.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new CreateDialogAdapter(contactList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_mark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checkMark:
                createDialog();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        List<String> listUid = new ArrayList<>();
        boolean[] check = adapter.getItemSelect();
        listUid.add(firebaseUser.getUid());
        int i = 0;
        for (Contact contact : contactList) {
            if (check[i]) {
                listUid.add(contact.getUid());
            }
            i++;
        }
        dialogFB.createNewDialog("Dialog", null, null, null, listUid, false);
    }
}
