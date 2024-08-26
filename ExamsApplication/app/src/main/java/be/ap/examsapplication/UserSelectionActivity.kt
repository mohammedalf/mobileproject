package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserSelectionActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersResultAdapter

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.toObjects(User::class.java)
                usersAdapter = UsersResultAdapter(users) { selectedUser ->
                    // Ga naar de UserResultsActivity voor de geselecteerde gebruiker
                    val intent = Intent(this, UserResultsActivity::class.java)
                    intent.putExtra("userName", "${selectedUser.firstName} ${selectedUser.lastName}")
                    startActivity(intent)
                }
                usersRecyclerView.adapter = usersAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
