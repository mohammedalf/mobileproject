package be.ap.examsapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class SelectUsersActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var saveSelectionButton: Button
    private lateinit var usersAdapter: UsersAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val selectedUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_users)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        saveSelectionButton = findViewById(R.id.saveSelectionButton)

        usersAdapter = UsersAdapter(emptyList()) { user, isSelected ->
            if (isSelected) {
                selectedUsers.add(user)
            } else {
                selectedUsers.remove(user)
            }
        }
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        saveSelectionButton.setOnClickListener {
            saveSelectedUsers()
        }

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

    private fun saveSelectedUsers() {
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra("selectedUsers", ArrayList(selectedUsers))
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
