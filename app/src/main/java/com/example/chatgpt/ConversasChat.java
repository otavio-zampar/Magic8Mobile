package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;


public class ConversasChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas_chat);
        AppCompatButton btnNovaConversa = findViewById(R.id.btnAddConversa);
        ConstraintLayout parentLayout = findViewById(R.id.parentLayout);


        btnNovaConversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatButton newButton = new AppCompatButton(getApplicationContext());
                newButton.setId(View.generateViewId());
                newButton.setText("New Button");

                // Set the constraints for the new button
                newButton.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT));

                // Add the new button to the layout
                parentLayout.addView(newButton);
            }
        });

    }





}
