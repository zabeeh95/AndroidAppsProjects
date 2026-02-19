package com.example.listpractise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.userlist);
        ArrayList<String> users = new ArrayList<>();

        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(aAdapter);

        users.add("1");
        users.add("2");
        users.add("3");
        users.add("4");
        users.add("5");
        users.add("6");
        users.add("7");
        users.add("8");
        users.add("9");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(view.getContext(),page2.class);
                    startActivity(intent);

                }

            }
        });


    }
}