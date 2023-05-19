package com.example.chatgpt

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val messageList: ArrayList<String> = ArrayList()
    private lateinit var messageAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Configurar o adaptador para a lista de mensagens
        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList)
        messageListView.adapter = messageAdapter

        // Configurar o clique do botão de envio
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotEmpty()) {
                addMessage(message)
                messageInput.text.clear()
            }
        }

        // Configurar o clique do botão de voltar
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addMessage(message: String) {
        messageList.add(message)
        messageAdapter.notifyDataSetChanged()
    }
}
