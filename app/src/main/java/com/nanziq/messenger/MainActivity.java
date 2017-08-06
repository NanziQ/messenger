package com.nanziq.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.nanziq.messenger.Adapters.DialogsAdapter;
import com.nanziq.messenger.Model.Dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<Dialog> dialogList = new ArrayList<>();
        dialogList.add(new Dialog("q1", "Брат", "https://img-fotki.yandex.ru/get/6836/13428093.31/0_c71e3_4748ecdf_orig", new Date(), "Hello"));
        dialogList.add(new Dialog("q2", "Сестра", "https://cs5.pikabu.ru/post_img/2014/06/24/10/1403625164_800406127.jpg", new Date(), "Hello bro"));
        DialogsAdapter adapter = new DialogsAdapter(dialogList, this);
        adapter.setListener(new DialogsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(),"Открытие диалога "+ position + "!",  Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
