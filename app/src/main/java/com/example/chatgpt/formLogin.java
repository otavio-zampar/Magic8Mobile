package com.example.chatgpt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class formLogin extends AppCompatActivity {


    EditText email, senha;
    Button BTNlogin;
    int a = 0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        TextView text_tela_cadastro = (TextView) findViewById(R.id.tela_cadastro);


        // muda a sombra para algo mais suave
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        BTNlogin = (Button) findViewById(R.id.BTNlogin);
        email.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33
        senha.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33



        BTNlogin.setOnClickListener(view -> {
            String mai = email.getText().toString();
            String pass = senha.getText().toString();

            if (mai.equals("") || pass.equals("")){
                Toast.makeText(formLogin.this, "verifique que nao existem espacos vazios", Toast.LENGTH_SHORT).show();
            }else {
                boolean checkPass;
                try (DBHelper DB = new DBHelper(this)){
                    checkPass = DB.checkLogin(mai, pass);
                }

                if (checkPass){
                    email.setText("");
                    senha.setText("");
                    Toast.makeText(formLogin.this, "logado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ConversasChat.class);
                    intent.putExtra("userEmail", mai);
                    startActivity(intent);
                }else{
                    Toast.makeText(formLogin.this, "Email ou senha nao condizem", Toast.LENGTH_SHORT).show();
                }
            }

        });

        findViewById(R.id.unfocus).setOnClickListener(v -> {

            email.clearFocus();
            senha.clearFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        });

        text_tela_cadastro.setOnClickListener(view -> {
            Intent intent = new Intent(formLogin.this, formCadastro.class);
            startActivity(intent);

        });


        senha.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.performClick();
                if(motionEvent.getRawX() >= (senha.getRight() - senha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    if (a == 0){
                        senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        a = 1;
                    }else{
                        senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        a = 0;
                    }

                    return true;
                }
            }
            return false;
        });

    }
}