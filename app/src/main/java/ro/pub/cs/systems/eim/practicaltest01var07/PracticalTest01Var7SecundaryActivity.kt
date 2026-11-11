package ro.pub.cs.systems.eim.practicaltest01var07

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var7SecundaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var07_secundary)
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

        // Afiseaza numerele primite de la prima activitate.
        val numbers = intent.getIntArrayExtra(Constants.EXTRA_NUMBERS)
        if (numbers != null) {
            fields.zip(numbers.toList()).forEach { (field, number) -> field.setText(number.toString()) }
        }

        findViewById<Button>(R.id.sum_button).setOnClickListener {
            sendResult("Sum", fields) { values -> values.sum() }
        }
        findViewById<Button>(R.id.product_button).setOnClickListener {
            sendResult("Product", fields) { values -> values.fold(1L) { acc, value -> acc * value } }
        }
    }

    // Calculeaza rezultatul din cele 4 campuri si il trimite inapoi primei activitati.
    private fun sendResult(operation: String, fields: List<EditText>, compute: (List<Long>) -> Long) {
        val values = fields.map { it.text.toString().toLongOrNull() }
        if (values.any { it == null }) {
            Toast.makeText(this, "All four fields must contain numbers", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
            .putExtra(Constants.EXTRA_OPERATION, operation)
            .putExtra(Constants.EXTRA_RESULT, compute(values.filterNotNull()))
        setResult(RESULT_OK, data)
        finish()
    }
}