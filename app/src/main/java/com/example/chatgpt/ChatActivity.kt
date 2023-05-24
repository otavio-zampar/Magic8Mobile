import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgpt.R

class ChatActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var chatTitle: TextView
    private lateinit var messageList: ListView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        backButton = findViewById(R.id.back_button)
        chatTitle = findViewById(R.id.chat_title)
        messageList = findViewById(R.id.message_list)
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        backButton.setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotEmpty()) {
                // Aqui você pode implementar a lógica para enviar a mensagem
                // e atualizar a lista de mensagens exibida na tela.
                // Por exemplo, você pode adicionar a mensagem à lista de mensagens
                // e notificar o adaptador para atualizar a exibição.
                // messageList.adapter.notifyDataSetChanged()

                // Limpar o campo de entrada de mensagens
                messageInput.text.clear()
            }
        }
    }
}
