package com.example.madt_lab5_makeyenka;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String[] display_list = {};
    ListView exchange_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exchange_list = (ListView) findViewById(R.id.exchange_list);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, display_list);
        exchange_list.setAdapter(adapter);
        DataLoader dl = new DataLoader();
        dl.loadPage();
    }
}