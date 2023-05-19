package com.example.chatgpt;

public class ChatSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        Button chatButton1 = findViewById(R.id.chatButton1);
        Button chatButton2 = findViewById(R.id.chatButton2);

        chatButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o clique no botão do Chat 1
                Toast.makeText(ChatSelectionActivity.this, "Clicou no Chat 1", Toast.LENGTH_SHORT).show();
            }
        });

        chatButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o clique no botão do Chat 2
                Toast.makeText(ChatSelectionActivity.this, "Clicou no Chat 2", Toast.LENGTH_SHORT).show();
            }
        });

        // Adicione lógica para os outros botões de chat, se houver
    }
}

