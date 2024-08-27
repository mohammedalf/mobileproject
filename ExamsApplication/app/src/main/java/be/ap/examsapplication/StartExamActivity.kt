package be.ap.examsapplication




import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.util.Locale

class StartExamActivity : AppCompatActivity() {

    private lateinit var examTitleTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var nextQuestionButton: Button
    private lateinit var previousQuestionButton: Button
    private lateinit var finishExamButton: Button
    private lateinit var openQuestionAnswerEditText: EditText
    private lateinit var multipleChoiceOptionsLayout: RadioGroup
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var option3RadioButton: RadioButton
    private lateinit var option4RadioButton: RadioButton
    private lateinit var endAndSaveButton: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var startTime: Long = 0
    private var currentQuestionIndex: Int = 0
    private lateinit var questions: List<Question>
    private val userAnswers = mutableListOf<String>()
    private var score: Double = 0.0
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_exam)

        examTitleTextView = findViewById(R.id.examTitleTextView)
        questionTextView = findViewById(R.id.questionTextView)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        previousQuestionButton = findViewById(R.id.previousQuestionButton)
        finishExamButton = findViewById(R.id.finishExamButton)
        openQuestionAnswerEditText = findViewById(R.id.openQuestionAnswerEditText)
        multipleChoiceOptionsLayout = findViewById(R.id.multipleChoiceOptionsLayout)
        option1RadioButton = findViewById(R.id.option1RadioButton)
        option2RadioButton = findViewById(R.id.option2RadioButton)
        option3RadioButton = findViewById(R.id.option3RadioButton)
        option4RadioButton = findViewById(R.id.option4RadioButton)
        endAndSaveButton = findViewById(R.id.endAndSaveButton)

        firestore = FirebaseFirestore.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val examId = intent.getStringExtra("examId")
        val userName = intent.getStringExtra("userName")

        if (examId != null) {
            loadExam(examId)
        } else {
            Toast.makeText(this, "Exam ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        startTime = System.currentTimeMillis()

        nextQuestionButton.setOnClickListener {
            saveAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            }
        }

        previousQuestionButton.setOnClickListener {
            saveAnswer()
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                loadQuestion()
            }
        }

        endAndSaveButton.setOnClickListener {
            saveAnswer()
            getLastLocation(userName)
        }

        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(userName: String?) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
                getAddressFromLocation(latitude!!, longitude!!) { addressResult ->
                    address = addressResult
                    finishExam(userName)
                }
            } ?: run {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                finishExam(userName)
            }
        }
    }
    private fun getAddressFromLocation(latitude: Double, longitude: Double, callback: (String) -> Unit) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                callback(address)
            } else {
                callback("Unknown Address")
            }
        } catch (e: Exception) {
            callback("Unknown Address")
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
                        userAnswers.clear()
                        userAnswers.addAll(List(questions.size) { "" })
                        loadQuestion()
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

    private fun loadQuestion() {
        if (currentQuestionIndex in questions.indices) {
            val question = questions[currentQuestionIndex]
            when (question) {
                is Question.OpenQuestion -> {
                    questionTextView.text = question.questionText
                    openQuestionAnswerEditText.visibility = View.VISIBLE
                    multipleChoiceOptionsLayout.visibility = View.GONE
                    openQuestionAnswerEditText.setText(userAnswers[currentQuestionIndex])
                }
                is Question.MultipleChoiceQuestion -> {
                    questionTextView.text = question.questionText
                    openQuestionAnswerEditText.visibility = View.GONE
                    multipleChoiceOptionsLayout.visibility = View.VISIBLE

                    option1RadioButton.text = question.options[0]
                    option2RadioButton.text = question.options[1]
                    option3RadioButton.text = question.options[2]
                    option4RadioButton.text = question.options[3]

                    // Restore the previously selected answer if it exists
                    when (userAnswers[currentQuestionIndex]) {
                        question.options[0] -> option1RadioButton.isChecked = true
                        question.options[1] -> option2RadioButton.isChecked = true
                        question.options[2] -> option3RadioButton.isChecked = true
                        question.options[3] -> option4RadioButton.isChecked = true
                        else -> multipleChoiceOptionsLayout.clearCheck()
                    }
                }
            }
        }
        updateButtonStates()
    }

    private fun updateButtonStates() {
        previousQuestionButton.isEnabled = currentQuestionIndex > 0
        nextQuestionButton.isEnabled = currentQuestionIndex < questions.size - 1
        finishExamButton.visibility = View.GONE
        endAndSaveButton.visibility = if (currentQuestionIndex == questions.size - 1) View.VISIBLE else View.GONE
    }

private fun saveAnswer() {
    val question = questions[currentQuestionIndex]
    val userAnswer: String

    when (question) {
        is Question.OpenQuestion -> {
            userAnswer = openQuestionAnswerEditText.text.toString()
            if (userAnswer.equals(question.answer, ignoreCase = true)) {
                score += 20.0 / questions.size
            }
        }
        is Question.MultipleChoiceQuestion -> {
            userAnswer = when {
                option1RadioButton.isChecked -> option1RadioButton.text.toString()
                option2RadioButton.isChecked -> option2RadioButton.text.toString()
                option3RadioButton.isChecked -> option3RadioButton.text.toString()
                option4RadioButton.isChecked -> option4RadioButton.text.toString()
                else -> ""
            }
            if (userAnswer == question.correctOption) {
                score += 20.0 / questions.size
            }
        }
        else -> userAnswer = ""
    }

    userAnswers[currentQuestionIndex] = userAnswer
}



private fun finishExam(userName: String?) {
    val endTime = System.currentTimeMillis()
    val examDuration = (endTime - startTime) / 1000

    val examResult = ExamResult(
        userName = userName ?: "",
        examTitle = examTitleTextView.text.toString(),
        date = Date(),
        duration = examDuration,
        score = score,
        answers = userAnswers,
        latitude = latitude,
        longitude = longitude,
        address = address
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
