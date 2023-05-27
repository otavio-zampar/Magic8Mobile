package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lilittlecat.chatgpt.offical.ChatGPT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

public class chat extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getSupportActionBar().hide();
        Intent i = getIntent();
        int ConvID = i.getIntExtra("ConvID", -1);
        int UserID = i.getIntExtra("UserID", -1);
//        ChatGPT2 GPT = new ChatGPT();
        ChatGPT chatGPT = new ChatGPT("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");
        DBHelper DB = new DBHelper(getApplicationContext());

        TextView title = findViewById(R.id.title);
        ImageButton send_button = findViewById(R.id.send_button);
        EditText message_input = findViewById(R.id.message_input);
        LinearLayout parentLayout = findViewById(R.id.textLayout);

        if (ConvID != -1) {
            title.setText(DB.getCvsName(UserID, ConvID));
        }else{
            title.setText("Nova Conversa");
        }

        send_button.setOnClickListener(view -> {

//            Toast.makeText(getApplicationContext(), GPT.realPromptBelieveIt(), Toast.LENGTH_SHORT).show();
            int MsgUsrID = createUserTextView(message_input.getText().toString(), parentLayout);

//            TextView textView = findViewById(MsgUsrID); // use when putting into DB
//            String textUsr = textView.getText().toString();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        int MsgChatID = createChatTextView(GPT.Prompt(message_input.getText().toString()), parentLayout);
                        int MsgChatID = createChatTextView(chatGPT.ask(message_input.getText().toString()), parentLayout);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();


//            int MsgChatID = createChatTextView(GPT.realPromptBelieveIt(), parentLayout);
//            textView = findViewById(MsgChatID); // use when putting into DB
//            String textChat = textView.getText().toString();

        });



    }

    private int createUserTextView(String msgUsr, LinearLayout parentLayout){

        int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.UsrComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
        newUsrTxt.setText(msgUsr);
        newUsrTxt.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        layoutParams.setMargins(dp8, dp8, dp8, dp8);
        newUsrTxt.setLayoutParams(layoutParams);
        parentLayout.addView(newUsrTxt, parentLayout.getChildCount());

        return newUsrTxt.getId();
    }

    private int createChatTextView(String answer, LinearLayout parentLayout){

        int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.ChatComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
//        newUsrTxt.setText(answer);
        newUsrTxt.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        layoutParams.setMargins(dp8, dp8, dp8, dp8);
        newUsrTxt.setLayoutParams(layoutParams);
        parentLayout.addView(newUsrTxt, parentLayout.getChildCount());


        long animationDelay = 70; // Delay in milliseconds

        newUsrTxt.setText("");
        Handler handler = new Handler();
        Runnable updateTextRunnable = new Runnable() {
            int charIndex = 0;

            @Override
            public void run() {
                if (charIndex < answer.length()) {
                    // Get the next character and append it to the TextView
                    char character = answer.charAt(charIndex);
                    newUsrTxt.append(String.valueOf(character));

                    // Increment the character index
                    charIndex = charIndex + 1;
                    // Post the Runnable to the Handler after the delay
                    handler.postDelayed(this, animationDelay);
                }
            }
        };
        // Start the animation by posting the Runnable to the Handler
        handler.post(updateTextRunnable);




        return newUsrTxt.getId();
    }
}
