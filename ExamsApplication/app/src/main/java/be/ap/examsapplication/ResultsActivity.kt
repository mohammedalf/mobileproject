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

/*class ResultsActivity : AppCompatActivity() {
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        val queryType = intent.getStringExtra("queryType")
        val queryValue = intent.getStringExtra("queryValue")

        Log.d("ResultsActivity", "Query Type: $queryType, Query Value: $queryValue") // Debugging

        when (queryType) {
            "user" -> loadResultsByUser(queryValue)
            "exam" -> loadResultsByExam(queryValue)
            else -> {
                Toast.makeText(this, "Invalid query type", Toast.LENGTH_SHORT).show()
                Log.e("ResultsActivity", "Received invalid query type: $queryType") // Debugging
            }
        }
    }

*//*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        val queryType = intent.getStringExtra("queryType")
        val queryValue = intent.getStringExtra("queryValue")

        when (queryType) {
            "user" -> loadResultsByUser(queryValue)
            "exam" -> loadResultsByExam(queryValue)
            else -> Toast.makeText(this, "Invalid query type", Toast.LENGTH_SHORT).show()
        }
    }*//*

    private fun loadResultsByUser(userName: String?) {
        firestore.collection("examResults")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.map { document ->
                    document.toObject(ExamResult::class.java)
                }
                resultsAdapter = ResultsAdapter(results, "user")
                resultsRecyclerView.adapter = resultsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadResultsByExam(examTitle: String?) {
        firestore.collection("examResults")
            .whereEqualTo("examTitle", examTitle)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.map { document ->
                    document.toObject(ExamResult::class.java)
                }
                resultsAdapter = ResultsAdapter(results, "exam")
                resultsRecyclerView.adapter = resultsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}*/

/*
class ResultsActivity : AppCompatActivity() {
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        val queryType = intent.getStringExtra("queryType")
        val queryValue = intent.getStringExtra("queryValue")

        when (queryType) {
            "user" -> loadResultsByUser(queryValue)
            "exam" -> loadResultsByExam(queryValue)
            else -> Toast.makeText(this, "Invalid query type", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadResultsByUser(userName: String?) {
        firestore.collection("examResults")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.map { document ->
                    document.toObject(ExamResult::class.java)
                }
                resultsAdapter = ResultsAdapter(results, "user")
                resultsRecyclerView.adapter = resultsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadResultsByExam(examTitle: String?) {
        firestore.collection("examResults")
            .whereEqualTo("examTitle", examTitle)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.map { document ->
                    document.toObject(ExamResult::class.java)
                }
                resultsAdapter = ResultsAdapter(results, "exam")
                resultsRecyclerView.adapter = resultsAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
*/
