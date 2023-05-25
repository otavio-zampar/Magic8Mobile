package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class formCadastro extends AppCompatActivity {

    EditText UserN, email, senha;
    AppCompatButton BTNcadastro;
    int a = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();


        UserN = findViewById(R.id.UserN);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        BTNcadastro = findViewById(R.id.BTNCadastro);
        DBHelper DB = new DBHelper(this);

        BTNcadastro.setOnClickListener(view -> {
                String usr = UserN.getText().toString();
                String mai = email.getText().toString();
                String pass = senha.getText().toString();


                if (usr.equals("") || mai.equals("") || pass.equals("")){
                    Toast.makeText(formCadastro.this, "verifique que não existem espacos vazios", Toast.LENGTH_SHORT).show();
                }else {
                    if(DB.checkMail(mai, pass) == false){
                        Boolean insert = DB.insertData(usr, mai, pass);
                        if (insert == true){
                            Toast.makeText(formCadastro.this, "Colocado certo, BELIEVE IT!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ConversasChat.class);
                            intent.putExtra("userEmail", mai);
                            startActivity(intent);
                        }else{
                            Toast.makeText(formCadastro.this, "erro ao criar usuário, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(formCadastro.this, "usuario ja existe!", Toast.LENGTH_SHORT).show();
                    }

                }
        });

        senha.setOnClickListener(view ->{
                        if (a == 0) {
                            senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            a = 1;
                        } else {
                            senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            a = 0;
                        }
        });

    }
}
