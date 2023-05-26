package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class chat extends AppCompatActivity {

    private LinearLayout parentLayout = findViewById(R.id.parentLayout);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getSupportActionBar().hide();
        Intent i = getIntent();
        String ConvID = i.getStringExtra("ConvID");
        String UserID = i.getStringExtra("UserID");
//        ChatGPT GPT = new ChatGPT();
        DBHelper DB = new DBHelper(getApplicationContext());

        TextView title = findViewById(R.id.title);
        ImageButton send_button = findViewById(R.id.send_button);
        EditText message_input = findViewById(R.id.message_input);

        if (ConvID != "") {
            title.setText(DB.getCvsName( Integer.valueOf(UserID), Integer.valueOf(ConvID)));
        }

        send_button.setOnClickListener(view -> {

//            Toast.makeText(this, "aaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
            String msgUsr = message_input.getText().toString();

            TextView newUsrTxt = new TextView(getApplicationContext());
            newUsrTxt.setId(TextView.generateViewId());
            newUsrTxt.setText(msgUsr);
            newUsrTxt.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());


            layoutParams.setMargins(8, 8, 8, 8);
            newUsrTxt.setLayoutParams(layoutParams);
            parentLayout.addView(newUsrTxt, 0);
            LinearLayout.LayoutParams newLayoutParams = (LinearLayout.LayoutParams) newUsrTxt.getLayoutParams();
            newLayoutParams.topMargin = dp8;
            newUsrTxt.setLayoutParams(newLayoutParams);


        });

//        TextView chat_title = findViewById(R.id.chat_title);
//        chat_title.setText(ConvID);



    }
}
