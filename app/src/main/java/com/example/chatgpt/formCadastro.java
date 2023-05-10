package com.example.chatgptformobile;

import androidx.appcompat.app.AppCompatActivity;

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
    Button BTNcadastro;
    int a = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();


        UserN = (EditText) findViewById(R.id.UserN);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        BTNcadastro = (Button) findViewById(R.id.BTNCadastro);
        DBHelper DB = new DBHelper(this);

        BTNcadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = UserN.getText().toString();
                String mai = email.getText().toString();
                String pass = senha.getText().toString();


                if (usr.equals("") || mai.equals("") || pass.equals("")){
                    Toast.makeText(formCadastro.this, "verifique que no existem espacos vazios", Toast.LENGTH_SHORT).show();
                }else {
                    if(DB.checkMail(mai, pass) == false){
                        Boolean insert = DB.insertData(usr, mai, pass);
                        if (insert == true){
                            Toast.makeText(formCadastro.this, "Colocado certo, BELIEVE IT!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ConversasChat.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(formCadastro.this, "erro ao criar usuário, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(formCadastro.this, "usuario ja existe!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        senha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
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
            }
        });

    }
}