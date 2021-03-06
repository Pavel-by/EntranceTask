package com.example.entrancetask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView        recycler;
    private RVAContacts         adapter;
    private LinearLayoutManager layoutManager;
    private boolean             isContactsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        adapter = new RVAContacts(this);
        layoutManager = new LinearLayoutManager(this);
        adapter.setButtonText("Повторить");
        adapter.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContacts();
            }
        });
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(
                    @NonNull RecyclerView recyclerView,
                    int dx,
                    int dy
            )
            {
                if (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 2) {
                    if (adapter.isLoadingEnabled()) loadContacts();
                }
            }
        });
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        loadContacts();
    }

    private void loadContacts() {
        if (isContactsLoading) return;
        adapter.setLoadingEnabled(true);
        isContactsLoading = true;
        WebHelper.loadContacts(new WebHelper.ContactsDownloadListener() {
            @Override
            public void onLoadingComplete(List<Contact> contacts) {
                adapter.addAll(contacts);
                isContactsLoading = false;
            }

            @Override
            public void onLoadingFailed() {
                adapter.setMessageText("При загрузке контактов произошла ошибка");
                adapter.setLoadingEnabled(false);
                isContactsLoading = false;
            }
        });
    }
}
