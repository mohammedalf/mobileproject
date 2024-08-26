package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ExamSelectionActivity : AppCompatActivity() {

    private lateinit var examsRecyclerView: RecyclerView
    private lateinit var examsAdapter: ExamsSelectionAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_selection)

        examsRecyclerView = findViewById(R.id.examsRecyclerView)
        examsRecyclerView.layoutManager = LinearLayoutManager(this)

        loadExams()
    }

    private fun loadExams() {
        firestore.collection("exams")
            .get()
            .addOnSuccessListener { documents ->
                val exams = documents.toObjects(Exam::class.java)
                examsAdapter = ExamsSelectionAdapter(exams) { selectedExam ->
                    // Ga naar ExamResultsActivity voor het geselecteerde examen
                    val intent = Intent(this, ExamResultsActivity::class.java)
                    intent.putExtra("examTitle", selectedExam.title)
                    startActivity(intent)
                }
                examsRecyclerView.adapter = examsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading exams: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
