import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatgpt.R

class MainActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var submitButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup = findViewById(R.id.radioGroup)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val selectedOption = radioGroup.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedOption)
            val selectedText = selectedRadioButton.text.toString()

            Toast.makeText(this, "Opção selecionada: $selectedText", Toast.LENGTH_SHORT).show()
        }
    }
}
