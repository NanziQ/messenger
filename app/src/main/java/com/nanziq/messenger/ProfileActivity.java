package com.nanziq.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nanziq.messenger.Adapters.ProfileAdapter;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Profile.EditNameActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ContactFB contactFB;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactFB = ContactFB.getInstance();
        Contact contact = contactFB.getContactFromUid(firebaseUser.getUid());
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(contact.getName());
        ImageView imageView = (ImageView) findViewById(R.id.main_backdrop);
        Glide
                .with(this)
                .load(contact.getImage())
                .into(imageView);
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
    }
}
