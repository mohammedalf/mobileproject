package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
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

        examsRecyclerView = findViewById(R.id.examsRecyclerView)

        examsAdapter = ExamsAdapter(emptyList()) { exam ->
            // Navigate to UsersActivity with the selected exam ID
            val intent = Intent(this, UsersActivity::class.java)
            intent.putExtra("examId", exam.id)
            startActivity(intent)
        }

        examsRecyclerView.adapter = examsAdapter
        examsRecyclerView.layoutManager = LinearLayoutManager(this)

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

/*class HomeActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val examsCollection = firestore.collection("exams")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)

        usersAdapter = UsersAdapter(emptyList()) { user, _ -> // Ignoring the second parameter (isSelected)
            // Retrieve the first exam ID from Firestore or add logic to select a specific exam
            examsCollection.limit(1).get()
                .addOnSuccessListener { result ->
                    val exam = result.documents.firstOrNull()
                    val examId = exam?.id

                    if (examId != null) {
                        val intent = Intent(this, StartExamActivity::class.java)
                        intent.putExtra("examId", examId)
                        intent.putExtra("userName", "${user.firstName} ${user.lastName}")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "No exams found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading exam: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)
                usersAdapter.setUsers(users)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}*/

/*

class HomeActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val examsCollection = firestore.collection("exams")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)

        usersAdapter = UsersAdapter(emptyList()) { user ->
            // Retrieve the first exam ID from Firestore or add logic to select a specific exam
            examsCollection.limit(1).get()
                .addOnSuccessListener { result ->
                    val exam = result.documents.firstOrNull()
                    val examId = exam?.id

                    if (examId != null) {
                        val intent = Intent(this, StartExamActivity::class.java)
                        intent.putExtra("examId", examId)
                        intent.putExtra("userName", "${user.firstName} ${user.lastName}")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "No exams found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading exam: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)
                usersAdapter.setUsers(users)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
*/
