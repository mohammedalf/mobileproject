package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val userResultsButton: Button = findViewById(R.id.userResultsButton)
        val examResultsButton: Button = findViewById(R.id.examResultsButton)

        userResultsButton.setOnClickListener {
            // Navigeren naar UserSelectionActivity om een gebruiker te kiezen
            val intent = Intent(this, UserSelectionActivity::class.java)
            startActivity(intent)
        }

        examResultsButton.setOnClickListener {
            // Navigeren naar ExamSelectionActivity om een examen te kiezen
            val intent = Intent(this, ExamSelectionActivity::class.java)
            startActivity(intent)
        }
    }
}



