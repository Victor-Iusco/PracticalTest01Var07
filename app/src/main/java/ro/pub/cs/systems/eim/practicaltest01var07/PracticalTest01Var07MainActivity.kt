package ro.pub.cs.systems.eim.practicaltest01var07

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import kotlin.random.Random

class PracticalTest01Var07MainActivity : AppCompatActivity() {

    // Campurile completate manual de utilizator; butonul Random nu le atinge.
    private val manuallySetFields = mutableSetOf<EditText>()

    // True cat timp textul este schimbat din cod, ca sa nu fie considerat editare manuala.
    private var isProgrammaticChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var07_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fields = listOf(
            findViewById<EditText>(R.id.top_left_text),
            findViewById<EditText>(R.id.top_right_text),
            findViewById<EditText>(R.id.bot_left_text),
            findViewById<EditText>(R.id.bot_right_text)
        )

        fields.forEach { field ->
            field.doAfterTextChanged { text ->
                if (isProgrammaticChange) return@doAfterTextChanged
                // Un camp golit de utilizator redevine disponibil pentru Random.
                if (text.isNullOrBlank()) {
                    manuallySetFields.remove(field)
                } else {
                    manuallySetFields.add(field)
                }
            }
        }

        findViewById<Button>(R.id.random_button).setOnClickListener {
            isProgrammaticChange = true
            fields.filter { it !in manuallySetFields }.forEach { field ->
                field.setText(Random.nextInt(0, 11).toString())
            }
            isProgrammaticChange = false
        }

        findViewById<Button>(R.id.navigate_button).setOnClickListener {
            val numbers = fields.map { it.text.toString().toIntOrNull() }
            if (numbers.any { it == null }) {
                Toast.makeText(this, "All four fields must contain numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, PracticalTest01Var7SecundaryActivity::class.java)
                .putExtra(Constants.EXTRA_NUMBERS, numbers.filterNotNull().toIntArray())
            secondaryActivityLauncher.launch(intent)
        }
    }

    // Primeste suma sau produsul calculat in a doua activitate si il afiseaza intr-un Toast.
    private val secondaryActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == RESULT_OK && data != null) {
                val operation = data.getStringExtra(Constants.EXTRA_OPERATION)
                val value = data.getLongExtra(Constants.EXTRA_RESULT, 0L)
                Toast.makeText(this, "$operation: $value", Toast.LENGTH_LONG).show()
            }
        }
}

object Constants {
    const val EXTRA_NUMBERS = "ro.pub.cs.systems.eim.practicaltest01var07.NUMBERS"
    const val EXTRA_OPERATION = "ro.pub.cs.systems.eim.practicaltest01var07.OPERATION"
    const val EXTRA_RESULT = "ro.pub.cs.systems.eim.practicaltest01var07.RESULT"
}
