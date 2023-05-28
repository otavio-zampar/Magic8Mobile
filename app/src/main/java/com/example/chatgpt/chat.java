package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class chat extends AppCompatActivity {

    TextToSpeech TTS;
    final Locale myLocale = new Locale("pt", "BR");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getSupportActionBar().hide();
        Intent i = getIntent();
        int ConvID = i.getIntExtra("ConvID", -1);
        int UserID = i.getIntExtra("UserID", -1);
        ChatGPT2 GPT = new ChatGPT2();
        DBHelper DB = new DBHelper(getApplicationContext());

        TextView title = findViewById(R.id.title);

        ImageButton send_button = findViewById(R.id.send_button);
        EditText message_input = findViewById(R.id.message_input);
        LinearLayout parentLayout = findViewById(R.id.textLayout);

        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Não foi possivel iniciar Text-To-Speech.", Toast.LENGTH_SHORT).show();
                }else {
                    Voice v = new Voice(TTS.getVoice().getName(), myLocale,400,200,false, null);
                    TTS.setVoice(v);
//                    TTS.setLanguage(myLocale);
                    TTS.setSpeechRate(1);
//                    Toast.makeText(getApplicationContext(), "Text-To-Speech iniciado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ConvID != -1) {
            title.setText(DB.getCvsName(UserID, ConvID));
        }else{
            title.setText("Nova Conversa");
        }

        send_button.setOnClickListener(view -> {
            if (!message_input.getText().toString().equals("")){
                int MsgUsrID = createUserTextView(message_input.getText().toString(), parentLayout);
                message_input.setText("");

                TextView textView = findViewById(MsgUsrID); // use when putting into DB
                String textUsr = textView.getText().toString();

                String txt = GPT.realPromptBelieveIt();
                int MsgChatID = createChatTextView(txt, parentLayout);
                textView = findViewById(MsgChatID); // use when putting into DB
                String textChat = textView.getText().toString();

                Bundle params = new Bundle();
                String utteranceId = UUID.randomUUID().toString();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);

                TTS.speak(txt, TTS.QUEUE_FLUSH, params, "MyUtteranceID");



            }

        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
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


        long animationDelay = 80; // Delay in milliseconds

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
