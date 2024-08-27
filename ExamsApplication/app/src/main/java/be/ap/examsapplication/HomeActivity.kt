package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
class HomeActivity : AppCompatActivity() {
    private lateinit var examsRecyclerView: RecyclerView
    private lateinit var examsAdapter: ExamsAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val examsCollection = firestore.collection("exams")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Available Exams"

        examsRecyclerView = findViewById(R.id.examsRecyclerView)

        examsAdapter = ExamsAdapter(emptyList()) { exam ->
            val intent = Intent(this, UsersActivity::class.java)
            intent.putExtra("examId", exam.id)
            startActivity(intent)
        }

        examsRecyclerView.adapter = examsAdapter
        examsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the admin login button click event
        findViewById<Button>(R.id.adminLoginButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        loadExams()
    }

    private fun loadExams() {
        examsCollection.get()
            .addOnSuccessListener { result ->
                val exams = result.toObjects(Exam::class.java)
                examsAdapter.setExams(exams)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading exams: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

