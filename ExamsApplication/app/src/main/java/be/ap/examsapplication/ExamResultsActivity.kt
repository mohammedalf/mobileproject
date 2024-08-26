package be.ap.examsapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ExamResultsActivity : AppCompatActivity() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ExamResultsAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_results)

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        val examTitle = intent.getStringExtra("examTitle")

        loadResultsByExam(examTitle)
    }

    private fun loadResultsByExam(examTitle: String?) {
        firestore.collection("examResults")
            .whereEqualTo("examTitle", examTitle)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.toObjects(ExamResult::class.java)
                resultsAdapter = ExamResultsAdapter(results)
                resultsRecyclerView.adapter = resultsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
