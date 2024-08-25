package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UsersActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private lateinit var examId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        examId = intent.getStringExtra("examId") ?: ""

        usersRecyclerView = findViewById(R.id.usersRecyclerView)

        usersAdapter = UsersAdapter(emptyList()) { user, _ ->
            // Start the exam for the selected user
            val intent = Intent(this, StartExamActivity::class.java)
            intent.putExtra("examId", examId)
            intent.putExtra("userName", "${user.firstName} ${user.lastName}")
            startActivity(intent)
        }

        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        firestore.collection("exams").document(examId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val exam = document.toObject(Exam::class.java)
                    val userIds = exam?.assignedUsers ?: emptyList()

                    if (userIds.isNotEmpty()) {
                        usersCollection.whereIn("id", userIds).get()
                            .addOnSuccessListener { result ->
                                val users = result.toObjects(User::class.java)
                                usersAdapter.setUsers(users)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "No users assigned to this exam", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Exam not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading exam: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
