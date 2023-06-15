package com.example.chatgpt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class chat extends AppCompatActivity {

    TextToSpeech TTS;
    int UserID = -1;
    final Locale myLocale = new Locale("pt", "BR");


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent i = getIntent();
        int ConvID = i.getIntExtra("ConvID", -1);
        UserID = i.getIntExtra("UserID", -1);

        try (DBHelper DB = new DBHelper(getApplicationContext())) {
            EditText title = findViewById(R.id.title);
            title.setOnFocusChangeListener((view, b) -> {
                if (!b) {
                    String a = title.getText().toString();
                    // literalmente só nao esta editando
                    DB.editCVSname(ConvID, a);
                    Toast.makeText(chat.this, ConvID + ", " + a + ", " + DB.getCvsName(UserID, ConvID), Toast.LENGTH_SHORT).show();
                }
            });

            ImageButton imgSidebar = findViewById(R.id.imgSidebar);
            EditText message_input = findViewById(R.id.message_input);
            LinearLayout parentLayout = findViewById(R.id.textLayout);

            // for every row in DB create a button
            for (int id = 0; id < DB.getMsgsRows(ConvID); id = id + 1) {
                int MsgUsrID = createUserTextView(DB.getPrompt(ConvID, id), parentLayout);
                int MsgChatID = createChatTextView2(DB.getAnswer(ConvID, id), parentLayout);

            }


            TTS = new TextToSpeech(this, status -> {
                if (status != TextToSpeech.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Não foi possivel iniciar Text-To-Speech.", Toast.LENGTH_SHORT).show();
                } else {
                    Voice v = new Voice(TTS.getVoice().getName(), myLocale, 400, 200, false, null);
                    TTS.setVoice(v);
                    //                    TTS.setLanguage(myLocale);
                    TTS.setSpeechRate(1);
                }
            });

            if (ConvID != DB.getConversaRows(UserID)) {
                title.setText(DB.getCvsName(UserID, ConvID));
            } else {
                title.setText(R.string.setText);
            }

            imgSidebar.setOnClickListener(view -> {

                Intent itnt = new Intent(getApplicationContext(), ConversasChat.class);
                itnt.putExtra("userEmail", DB.getEmail(UserID));
                startActivity(itnt);

            });

            message_input.setOnTouchListener((view, motionEvent) -> {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                    if(motionEvent.getRawX() >= (message_input.getRight() - message_input.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (!message_input.getText().toString().equals("")) {

                            message_input.clearFocus();
                            ScrollView SV = findViewById(R.id.SV);
                            SV.fullScroll(ScrollView.FOCUS_DOWN);

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                            int MsgUsrID = createUserTextView(message_input.getText().toString(), parentLayout);
                            message_input.setText("");

                            TextView textView = findViewById(MsgUsrID); // use when putting into DB
                            String textUsr = textView.getText().toString();

                            String txt = ChatGPT2.realPromptBelieveIt();
                            int MsgChatID = createChatTextView(txt, parentLayout);
                            textView = findViewById(MsgChatID); // use when putting into DB
//                            String textChat = textView.getText().toString();

                            Bundle params = new Bundle();
                            String utteranceId = UUID.randomUUID().toString();
                            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);

                            TTS.speak(txt, TextToSpeech.QUEUE_FLUSH, params, "MyUtteranceID");
                            if (!(DB.insertMsgs(ConvID-1, textUsr, txt))) {
                                Toast.makeText(this, "Não foi possível enviar a mensagem para o banco", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                }
                return false;
            });
        }
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

        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.UsrComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
        newUsrTxt.setText(msgUsr);
        newUsrTxt.setGravity(Gravity.START);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newUsrTxt.setPadding(dp1*90, dp1*30, dp1*60, dp1*30);
        newUsrTxt.setLayoutParams(layoutParams);
        parentLayout.addView(newUsrTxt, parentLayout.getChildCount());

        return newUsrTxt.getId();
    }

    private int createChatTextView(String answer, LinearLayout parentLayout){

        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.ChatComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
//        newUsrTxt.setText(answer);
        newUsrTxt.setGravity(Gravity.START);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newUsrTxt.setPadding(dp1*90, dp1*30, dp1*60, dp1*30);
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

        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.ChatComponents);
        TextView newUsrTxt = new TextView(newContext);
        newUsrTxt.setId(TextView.generateViewId());
        newUsrTxt.setText(answer);
        newUsrTxt.setGravity(Gravity.START);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newUsrTxt.setPadding(dp1*90, dp1*30, dp1*60, dp1*30);
        newUsrTxt.setLayoutParams(layoutParams);
        parentLayout.addView(newUsrTxt, parentLayout.getChildCount());

        return newUsrTxt.getId();
    }
}
