package com.example.listpractise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class page2 extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        textView = findViewById(R.id.page_text);
        editText = findViewById(R.id.editpage2);
        button = findViewById(R.id.buttonadd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbutton();
            }
        });


    }

    public void addbutton() {
        String st = editText.getText().toString();
        textView.setText(st);
    }
}