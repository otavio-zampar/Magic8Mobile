package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class chat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getSupportActionBar().hide();
        Intent i = getIntent();
        String ConvID = i.getStringExtra("ConvID");
        ChatGPT GPT = new ChatGPT();

//        TextView chat_title = findViewById(R.id.chat_title);
//        chat_title.setText(ConvID);

    }
}
