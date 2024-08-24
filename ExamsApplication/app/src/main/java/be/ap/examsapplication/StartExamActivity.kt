package be.ap.examsapplication

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.Locale


import android.Manifest

class StartExamActivity : AppCompatActivity() {
    private lateinit var examTitleTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var submitAnswerButton: Button
    private lateinit var finishExamButton: Button

    private lateinit var firestore: FirebaseFirestore

    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_exam)

        examTitleTextView = findViewById(R.id.examTitleTextView)
        questionTextView = findViewById(R.id.questionTextView)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        finishExamButton = findViewById(R.id.finishExamButton)

        firestore = FirebaseFirestore.getInstance()

        val userName = intent.getStringExtra("userName")
        val examTitle = "Sample Exam Title" // Retrieve this dynamically if needed
        examTitleTextView.text = examTitle

        // Start exam and track start time
        startTime = System.currentTimeMillis()

        // Load the first question (this should be dynamic)
        loadNextQuestion()

        submitAnswerButton.setOnClickListener {
            // Handle answer submission
            // Save the answer and load the next question or finish the exam
            loadNextQuestion()
        }

        finishExamButton.setOnClickListener {
            finishExam(userName, examTitle)
        }
    }

    private fun loadNextQuestion() {
        // Dynamically load the next question
        // If the exam is complete, show the finish button
        val nextQuestion = "Sample question text"
        questionTextView.text = nextQuestion
        submitAnswerButton.text = "Submit Answer"

        // If it's the last question:
        // finishExamButton.visibility = View.VISIBLE
    }

    private fun finishExam(userName: String?, examTitle: String) {
        val endTime = System.currentTimeMillis()
        val examDuration = (endTime - startTime) / 1000 // in seconds
        val examResult = mapOf(
            "userName" to userName,
            "examTitle" to examTitle,
            "date" to Date(),
            "duration" to examDuration,
            "score" to 100 // Replace with actual score calculation
        )

        firestore.collection("examResults").add(examResult)
            .addOnSuccessListener {
                Toast.makeText(this, "Exam finished and results saved", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}