package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.chatgpt.R;

public class ConversasChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas_chat);

        LinearLayout buttonLayout = findViewById(R.id.button_layout);

        for (int i = 0; i < 3; i++) {
            Button button = new Button(this);
            button.setText("Button " + (i + 1));
            buttonLayout.addView(button);

            if (i == 2) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button newButton = new Button(ConversasChat.this);
                        newButton.setText("New Button");
                        buttonLayout.addView(newButton, 0);
                    }
                });
            }
        }
    }
}
