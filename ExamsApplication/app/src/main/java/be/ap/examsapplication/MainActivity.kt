package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usersManagementButton: Button = findViewById(R.id.usersManagementButton)
        val examsManagementButton: Button = findViewById(R.id.examsManagementButton)
        val resultsButton: Button = findViewById(R.id.resultsButton)
        val settingsButton: Button = findViewById(R.id.settingsButton)

        // Navigatie naar Users Management
        usersManagementButton.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        // Voeg hier soortgelijke navigatie toe voor Exams Management, Results en Settings
        examsManagementButton.setOnClickListener {
            // TODO: Voeg intent toe naar Exams Management Activity
        }

        resultsButton.setOnClickListener {
            // TODO: Voeg intent toe naar Results Activity
        }

        settingsButton.setOnClickListener {
            // TODO: Voeg intent toe naar Settings Activity
        }
    }
}