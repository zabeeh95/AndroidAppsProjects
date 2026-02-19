package com.example.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<String> wordList;

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wordList = new ArrayList<>();

        for (int i = 0; i <20 ; i++) {
            wordList.add("present  " + i);
        }


        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new WordListAdapter(this, wordList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new_method();

    }

    public  void new_method(){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                wordList.add(".................breaker...............");
                for (int i = 0; i <4 ; i++) {
                    wordList.add(i,"new words " + i);
                    mAdapter.notifyItemInserted(0);
                }

//                mAdapter.notifyDataSetChanged();



            new_method();
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}