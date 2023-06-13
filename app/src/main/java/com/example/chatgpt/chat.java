package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class chat extends AppCompatActivity {

    TextToSpeech TTS;
    final Locale myLocale = new Locale("pt", "BR");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent i = getIntent();
        int ConvID = i.getIntExtra("ConvID", -1);
        int UserID = i.getIntExtra("UserID", -1);
        DBHelper DB = new DBHelper(getApplicationContext());

        EditText title = findViewById(R.id.title);

        title.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                String a = title.getText().toString();
                DB.editCVSname(ConvID, a);
                Toast.makeText(chat.this, ConvID + ", " + a, Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton imgSidebar = findViewById(R.id.imgSidebar);

        ImageButton send_button = findViewById(R.id.send_button);
        EditText message_input = findViewById(R.id.message_input);
        LinearLayout parentLayout = findViewById(R.id.textLayout);

        // for every row in DB create a button and increment to Nconversas with name "conversas(nome)"
        for (int id = 0; id < DB.getMsgsRows(ConvID); id = id+1) {
            int MsgUsrID = createUserTextView(DB.getPrompt(ConvID, id), parentLayout);
            int MsgChatID = createChatTextView2(DB.getAnswer(ConvID, id), parentLayout);

        }



        TTS = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(getApplicationContext(), "Não foi possivel iniciar Text-To-Speech.", Toast.LENGTH_SHORT).show();
            }else {
                Voice v = new Voice(TTS.getVoice().getName(), myLocale,400,200,false, null);
                TTS.setVoice(v);
//                    TTS.setLanguage(myLocale);
                TTS.setSpeechRate(1);
            }
        });

        if (ConvID != -1) {
            title.setText(DB.getCvsName(UserID, ConvID));
        }else{
            title.setText("Nova Conversa");
        }

        imgSidebar.setOnClickListener(view -> {

            Intent itnt = new Intent(getApplicationContext(), ConversasChat.class);
            itnt.putExtra("userEmail", DB.getEmail(UserID));
            startActivity(itnt);

        });

        send_button.setOnClickListener(view -> {
            if (!message_input.getText().toString().equals("")){
                int MsgUsrID = createUserTextView(message_input.getText().toString(), parentLayout);
                message_input.setText("");

                TextView textView = findViewById(MsgUsrID); // use when putting into DB
                String textUsr = textView.getText().toString();

                String txt = ChatGPT2.realPromptBelieveIt();
                int MsgChatID = createChatTextView(txt, parentLayout);
                textView = findViewById(MsgChatID); // use when putting into DB
//                String textChat = textView.getText().toString();

                Bundle params = new Bundle();
                String utteranceId = UUID.randomUUID().toString();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);

                TTS.speak(txt, TextToSpeech.QUEUE_FLUSH, params, "MyUtteranceID");
                if(!(DB.insertMsgs(ConvID, textUsr, txt))){
                    Toast.makeText(this, "Não foi possível enviar a mensagem para o banco", Toast.LENGTH_SHORT).show();
                }
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
        newUsrTxt.setGravity(Gravity.START);

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
        newUsrTxt.setGravity(Gravity.START);

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

    private int createChatTextView2(String answer, LinearLayout parentLayout){

        int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.ChatComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
        newUsrTxt.setText(answer);
        newUsrTxt.setGravity(Gravity.START);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        layoutParams.setMargins(dp8, dp8, dp8, dp8);
        newUsrTxt.setLayoutParams(layoutParams);
        parentLayout.addView(newUsrTxt, parentLayout.getChildCount());

        return newUsrTxt.getId();
    }
}
