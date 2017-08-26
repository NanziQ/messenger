package com.nanziq.messenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nanziq.messenger.Adapters.ContactsAdapter;
import com.nanziq.messenger.Adapters.DialogsAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.DialogView;
import com.nanziq.messenger.Model.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MessagesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ContactFB contactFB;
    private DialogFB dialogFB;
    private FirebaseRecyclerAdapter<DialogView, DialogsAdapter.ViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference databaseReference;
    private Query recyclerQuery;
    private List<Dialog> dialogList;
    private DialogsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactFB = ContactFB.getInstance();
        dialogFB = DialogFB.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        String uid = firebaseUser.getUid();
//        dialogList = dialogFB.getContactDialogList(uid);
//        adapter = new DialogsAdapter(dialogList, this);
        recyclerQuery = databaseReference.child("dialogs").orderByChild("contacts").equalTo(uid);
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DialogView, DialogsAdapter.ViewHolder>(DialogView.class,
//                R.layout.cardview_dialogs,
//                DialogsAdapter.ViewHolder.class,
//                recyclerQuery) {
//            @Override
//            protected void populateViewHolder(DialogsAdapter.ViewHolder viewHolder, DialogView model, int position) {
//                viewHolder.binding.setDialog(model);
//                viewHolder.setListener(new DialogsAdapter.ViewHolder.Listener() {
//                    @Override
//                    public void onClick(View view, int position) {
//                        Intent intent = new Intent(getApplicationContext(), DialogViewActivity.class);
//                        intent.putExtra("dialogId", firebaseRecyclerAdapter.getRef(position).getKey());
//                        startActivity(intent);
//                    }
//                });
//            }
//        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        downloadData();
//        recyclerView.setAdapter(adapter);
    }

    public class DownloadData extends AsyncTask<String, Void, Void>{
        private DialogFB dialogFB1 = DialogFB.getInstance();
        private DialogsAdapter adapter;
        private List<Dialog> dialogs = null;
        @Override
        protected Void doInBackground(final String... id) {

            while (true) {
                if (dialogs == null) {
                    try {
                        Thread.sleep(500);
                        dialogs = dialogFB1.getContactDialogList(id[0]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    adapter = new DialogsAdapter(dialogs, getApplicationContext());
                    return null;

                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setListener(new DialogsAdapter.Listener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(getApplicationContext(), DialogViewActivity.class);
                    intent.putExtra("dialogId", dialogs.get(position).getId());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void downloadData(){
       DownloadData downloadData = new DownloadData();
        downloadData.execute(firebaseUser.getUid());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_contacts) {
            startActivity(new Intent(this, ContactsActivity.class));
        } else if (id == R.id.nav_settings) {

        }else if (id == R.id.nav_exit) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
