package be.ap.examsapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AddExamActivity : AppCompatActivity(){
    private lateinit var examTitleEditText: EditText
    private lateinit var addOpenQuestionButton: Button
    private lateinit var addMultipleChoiceQuestionButton: Button
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var saveExamButton: Button
    private lateinit var questionsAdapter: QuestionsAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val examsCollection = firestore.collection("exams")

    private val questions = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exam)

        examTitleEditText = findViewById(R.id.examTitleEditText)
        addOpenQuestionButton = findViewById(R.id.addOpenQuestionButton)
        addMultipleChoiceQuestionButton = findViewById(R.id.addMultipleChoiceQuestionButton)
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView)
        saveExamButton = findViewById(R.id.saveExamButton)

        questionsAdapter = QuestionsAdapter(questions)
        questionsRecyclerView.adapter = questionsAdapter
        questionsRecyclerView.layoutManager = LinearLayoutManager(this)

        addOpenQuestionButton.setOnClickListener {
            showOpenQuestionDialog()
        }

        addMultipleChoiceQuestionButton.setOnClickListener {
            showMultipleChoiceQuestionDialog()
        }

        saveExamButton.setOnClickListener {
            saveExam()
        }
    }

    private fun showOpenQuestionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_open_question, null)
        val questionEditText = dialogView.findViewById<EditText>(R.id.openQuestionEditText)

        AlertDialog.Builder(this)
            .setTitle("Add Open Question")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val questionText = questionEditText.text.toString().trim()
                if (questionText.isNotEmpty()) {
                    questions.add(Question.OpenQuestion(questionText))
                    questionsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Question cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showMultipleChoiceQuestionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_multiple_choice_question, null)
        val questionEditText = dialogView.findViewById<EditText>(R.id.multipleChoiceQuestionEditText)
        val option1EditText = dialogView.findViewById<EditText>(R.id.option1EditText)
        val option2EditText = dialogView.findViewById<EditText>(R.id.option2EditText)
        val option3EditText = dialogView.findViewById<EditText>(R.id.option3EditText)
        val option4EditText = dialogView.findViewById<EditText>(R.id.option4EditText)
        val correctOptionEditText = dialogView.findViewById<EditText>(R.id.correctOptionEditText)

        AlertDialog.Builder(this)
            .setTitle("Add Multiple Choice Question")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val questionText = questionEditText.text.toString().trim()
                val options = listOf(
                    option1EditText.text.toString().trim(),
                    option2EditText.text.toString().trim(),
                    option3EditText.text.toString().trim(),
                    option4EditText.text.toString().trim()
                )
                val correctOption = correctOptionEditText.text.toString().trim()

                if (questionText.isNotEmpty() && options.all { it.isNotEmpty() } && correctOption.isNotEmpty()) {
                    questions.add(
                        Question.MultipleChoiceQuestion(
                            questionText,
                            options,
                            correctOption
                        )
                    )
                    questionsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveExam() {
        val examTitle = examTitleEditText.text.toString().trim()

        if (examTitle.isNotEmpty() && questions.isNotEmpty()) {
            val exam = Exam(title = examTitle, questions = questions)
            examsCollection.add(exam)
                .addOnSuccessListener {
                    Toast.makeText(this, "Exam saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving exam: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Title and questions are required", Toast.LENGTH_SHORT).show()
        }
    }
}