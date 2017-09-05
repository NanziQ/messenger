package com.nanziq.messenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nanziq.messenger.Adapters.ProfileAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Profile.EditImageActivity;
import com.nanziq.messenger.Profile.EditNameActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ContactFB contactFB;
    private RecyclerView recyclerView;
    private Contact contact;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactFB = ContactFB.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        contact = contactFB.getContactFromUid(firebaseUser.getUid());
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(contact.getName());
        imageView = (ImageView) findViewById(R.id.main_backdrop);

        recyclerView = (RecyclerView) findViewById(R.id.profile_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.profile_strings));
        ProfileAdapter adapter = new ProfileAdapter(list);
        adapter.setOnClickListener(new ProfileAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(), EditImageActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), EditNameActivity.class));
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

        databaseReference.child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                downloadImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadImage(){
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute();
    }
    public class DownloadImage extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            contact = contactFB.getContactFromUid(firebaseUser.getUid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Glide
                    .with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child("images/" + contact.getImage()))
                    .placeholder(R.drawable.ic_account_black_48dp)
                    .into(imageView);
        }
    }
}
