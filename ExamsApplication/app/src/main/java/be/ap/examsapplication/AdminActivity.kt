package be.ap.examsapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

//data class User(val firstName: String = "", val lastName: String = "")

class AdminActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var addUserButton: Button
    private lateinit var multipleUsersEditText: EditText
    private lateinit var addMultipleUsersButton: Button
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        addUserButton = findViewById(R.id.addUserButton)
        multipleUsersEditText = findViewById(R.id.multipleUsersEditText)
        addMultipleUsersButton = findViewById(R.id.addMultipleUsersButton)
        usersRecyclerView = findViewById(R.id.usersRecyclerView)

        // Initialize RecyclerView
        usersAdapter = UsersAdapter(mutableListOf()) {} // Empty initial list and no click handling
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        // Add single user to Firestore
        addUserButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()

            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val user = User(firstName, lastName)
                usersCollection.add(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                        firstNameEditText.text.clear()
                        lastNameEditText.text.clear()
                        loadUsers() // Refresh the list after adding
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Add multiple users to Firestore
        addMultipleUsersButton.setOnClickListener {
            val usersText = multipleUsersEditText.text.toString().trim()

            if (usersText.isNotEmpty()) {
                val usersList = usersText.lines().mapNotNull { line ->
                    val words = line.trim().split("\\s+".toRegex())
                    if (words.size >= 2) {
                        val firstName = words.firstOrNull { it.isNotEmpty() && it[0].isUpperCase() }
                        val lastName = words.drop(1).joinToString(" ") { if (it.isNotEmpty() && it[0].isUpperCase()) it else "" }.trim()
                        if (!firstName.isNullOrEmpty() && lastName.isNotEmpty()) {
                            User(firstName, lastName)
                        } else null
                    } else null
                }

                usersList.forEach { user ->
                    usersCollection.add(user)
                        .addOnSuccessListener {
                            // Optional: Show success message or update UI
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                Toast.makeText(this, "Multiple users added", Toast.LENGTH_SHORT).show()
                multipleUsersEditText.text.clear()
                loadUsers() // Refresh the list after adding
            } else {
                Toast.makeText(this, "Please paste a list of users", Toast.LENGTH_SHORT).show()
            }
        }

        // Load users from Firestore
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