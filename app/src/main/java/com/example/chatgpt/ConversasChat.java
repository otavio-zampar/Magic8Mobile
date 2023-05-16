package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;


public class ConversasChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas_chat);
        AppCompatButton btnNovaConversa = findViewById(R.id.btnAddConversa);
        ImageButton imgAdd = findViewById(R.id.add);
        ConstraintLayout parentLayout = findViewById(R.id.parentLayout);


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButton(btnNovaConversa, parentLayout);
            }
        });

    }

    private int lastButtonId = View.NO_ID;  // Store the ID of the last created button

    @SuppressLint("ResourceType")
    protected void createButton(AppCompatButton btnNovaConversa, ConstraintLayout parentLayout) {
        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.BTNComponents);
        AppCompatButton newButton = new AppCompatButton(newContext);
        newButton.setId(View.generateViewId());
        newButton.setText("Nova Conversa");
        newButton.setGravity(Gravity.CENTER);


        // Set the constraints for the new button
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        // Set constraints for the button within the ConstraintLayout
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        int dp25 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        int dp5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        layoutParams.setMargins(dp25, dp5, dp25, 0);

        if (lastButtonId != View.NO_ID) {
            layoutParams.topToBottom = lastButtonId;
        } else {
            layoutParams.topToBottom = R.id.navbar;
        }

        newButton.setLayoutParams(layoutParams);

        // Add the new button to the layout
        parentLayout.addView(newButton);

        // Update the ID of the last created button
        lastButtonId = newButton.getId();

        // Update the constraints for btnNovaConversa
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) btnNovaConversa.getLayoutParams();
        newLayoutParams.topToBottom = lastButtonId;
        newLayoutParams.topMargin = dp5;
        btnNovaConversa.setLayoutParams(newLayoutParams);
    }