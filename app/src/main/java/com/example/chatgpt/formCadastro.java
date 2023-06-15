package com.example.chatgpt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class formCadastro extends AppCompatActivity {

    EditText UserN, email, senha;
    AppCompatButton BTNcadastro;
    int a = 0;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        Objects.requireNonNull(getSupportActionBar()).hide();


        UserN = findViewById(R.id.UserN);
        UserN.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33
        email = findViewById(R.id.email);
        email.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33
        EditText Cemail = findViewById(R.id.C_email);
        Cemail.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33
        senha = findViewById(R.id.senha);
        senha.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33
        BTNcadastro = findViewById(R.id.BTNCadastro);
        BTNcadastro.setOutlineSpotShadowColor(Color.rgb(42, 43, 51)); // #2A2B33




        BTNcadastro.setOnClickListener(view -> {
                String usr = UserN.getText().toString();
                String mai = email.getText().toString();
                String pass = senha.getText().toString();


                if (usr.equals("") || mai.equals("") || pass.equals("")){
                    Toast.makeText(formCadastro.this, "verifique que não existem espacos vazios", Toast.LENGTH_SHORT).show();
                }else {
                    try (DBHelper DB = new DBHelper(this)) {
                        if (!DB.checkMail(mai, pass)) {
                            boolean insert = DB.insertData(usr, mai, pass);
                            if (insert) {
                                UserN.setText("");
                                email.setText("");
                                senha.setText("");
                                Toast.makeText(formCadastro.this, "Colocado certo, BELIEVE IT!!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ConversasChat.class);
                                intent.putExtra("userEmail", mai);
                                startActivity(intent);
                            } else {
                                Toast.makeText(formCadastro.this, "erro ao criar usuário, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(formCadastro.this, "usuario ja existe!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
