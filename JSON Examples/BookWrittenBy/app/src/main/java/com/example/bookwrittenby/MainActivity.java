package com.example.bookwrittenby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText inputBookTextView;
    private TextView titleTextView;
    private TextView authorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBookTextView = (EditText)findViewById(R.id.bookInput);
        titleTextView = (TextView)findViewById(R.id.titleText);
        authorTextView = (TextView)findViewById(R.id.authorText);
    }

    public void searchBooks(View view) {
        String queryString = inputBookTextView.getText().toString();

        InputMethodManager inputManager = (InputMethodManager)      //hide keyboard when not in use
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager)         //connectivity check of internet connection
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            new FetchBook(titleTextView, authorTextView).execute(queryString);
            authorTextView.setText("");
            titleTextView.setText(R.string.loading);
        } else {
            if (queryString.length() == 0) {
                authorTextView.setText("");
                titleTextView.setText(R.string.no_search_term);
            } else {
                authorTextView.setText("");
                titleTextView.setText(R.string.no_network);
            }
        }
    }
}