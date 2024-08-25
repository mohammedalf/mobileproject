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
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup

class StartExamActivity : AppCompatActivity() {

    private lateinit var examTitleTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var submitAnswerButton: Button
    private lateinit var finishExamButton: Button
    private lateinit var openQuestionAnswerEditText: EditText
    private lateinit var multipleChoiceOptionsLayout: RadioGroup
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var option3RadioButton: RadioButton
    private lateinit var option4RadioButton: RadioButton

    private lateinit var firestore: FirebaseFirestore

    private var startTime: Long = 0
    private var currentQuestionIndex: Int = 0
    private lateinit var questions: List<Question>
    private var userAnswers: MutableList<String> = mutableListOf()
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_exam)

        examTitleTextView = findViewById(R.id.examTitleTextView)
        questionTextView = findViewById(R.id.questionTextView)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        finishExamButton = findViewById(R.id.finishExamButton)
        openQuestionAnswerEditText = findViewById(R.id.openQuestionAnswerEditText)
        multipleChoiceOptionsLayout = findViewById(R.id.multipleChoiceOptionsLayout)
        option1RadioButton = findViewById(R.id.option1RadioButton)
        option2RadioButton = findViewById(R.id.option2RadioButton)
        option3RadioButton = findViewById(R.id.option3RadioButton)
        option4RadioButton = findViewById(R.id.option4RadioButton)

        firestore = FirebaseFirestore.getInstance()

        val examId = intent.getStringExtra("examId")
        val userName = intent.getStringExtra("userName")

        if (examId != null) {
            loadExam(examId)
        } else {
            Toast.makeText(this, "Exam ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        startTime = System.currentTimeMillis()

        submitAnswerButton.setOnClickListener {
            submitAnswer()
        }

        finishExamButton.setOnClickListener {
            finishExam(userName)
        }
    }

    private fun loadExam(examId: String) {
        Log.d("StartExamActivity", "Loading exam with ID: $examId")
        firestore.collection("exams").document(examId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val exam = document.toObject(Exam::class.java)
                    if (exam != null) {
                        examTitleTextView.text = exam.title
                        questions = exam.questions.map { questionMap ->
                            val type = questionMap["type"] as String
                            when (type) {
                                "OpenQuestion" -> {
                                    Question.OpenQuestion(
                                        questionText = questionMap["questionText"] as String,
                                        answer = questionMap["answer"] as String
                                    )
                                }
                                "MultipleChoiceQuestion" -> {
                                    Question.MultipleChoiceQuestion(
                                        questionText = questionMap["questionText"] as String,
                                        options = (questionMap["options"] as List<String>),
                                        correctOption = questionMap["correctOption"] as String
                                    )
                                }
                                else -> throw IllegalArgumentException("Unknown question type: $type")
                            }
                        }
                        loadNextQuestion()
                    } else {
                        Toast.makeText(this, "Exam data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Exam not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading exam: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadNextQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            when (question) {
                is Question.OpenQuestion -> {
                    questionTextView.text = question.questionText
                    openQuestionAnswerEditText.visibility = View.VISIBLE
                    multipleChoiceOptionsLayout.visibility = View.GONE
                }
                is Question.MultipleChoiceQuestion -> {
                    questionTextView.text = question.questionText
                    openQuestionAnswerEditText.visibility = View.GONE
                    multipleChoiceOptionsLayout.visibility = View.VISIBLE

                    option1RadioButton.text = question.options[0]
                    option2RadioButton.text = question.options[1]
                    option3RadioButton.text = question.options[2]
                    option4RadioButton.text = question.options[3]
                }
            }
        } else {
            finishExamButton.visibility = View.VISIBLE
            submitAnswerButton.visibility = View.GONE
        }
    }

    private fun submitAnswer() {
        val question = questions[currentQuestionIndex]
        val userAnswer: String

        when (question) {
            is Question.OpenQuestion -> {
                userAnswer = openQuestionAnswerEditText.text.toString()
                userAnswers.add(userAnswer)
                openQuestionAnswerEditText.text.clear()
            }
            is Question.MultipleChoiceQuestion -> {
                userAnswer = when {
                    option1RadioButton.isChecked -> option1RadioButton.text.toString()
                    option2RadioButton.isChecked -> option2RadioButton.text.toString()
                    option3RadioButton.isChecked -> option3RadioButton.text.toString()
                    option4RadioButton.isChecked -> option4RadioButton.text.toString()
                    else -> ""
                }
                userAnswers.add(userAnswer)
                if (userAnswer == question.correctOption) {
                    score += 1
                }
                multipleChoiceOptionsLayout.clearCheck()
            }
        }

        currentQuestionIndex++
        loadNextQuestion()
    }

    private fun finishExam(userName: String?) {
        val endTime = System.currentTimeMillis()
        val examDuration = (endTime - startTime) / 1000

        val examResult = mapOf(
            "userName" to userName,
            "examTitle" to examTitleTextView.text.toString(),
            "date" to Date(),
            "duration" to examDuration,
            "score" to score,
            "answers" to userAnswers
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
