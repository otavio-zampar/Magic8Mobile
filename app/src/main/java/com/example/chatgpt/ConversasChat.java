package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
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

    protected void createButton(AppCompatButton btnNovaConversa, ConstraintLayout parentLayout) {
        AppCompatButton newButton = new AppCompatButton(getApplicationContext());
        newButton.setId(View.generateViewId());
        newButton.setText("New Button");
        newButton.setBackground(getDrawable(R.drawable.btn_components));

        // Set the constraints for the new button
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        // Set constraints for the button within the ConstraintLayout
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

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
        btnNovaConversa.setLayoutParams(newLayoutParams);
    }

}
