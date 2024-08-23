package be.ap.examsapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Firebase auth instance
        auth = FirebaseAuth.getInstance()

        // Get references to the UI elements
        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        // Display the user's email
        val userEmail = auth.currentUser?.email
        welcomeTextView.text = "Welcome, $userEmail!"

        // Set up the logout button
        logoutButton.setOnClickListener {
            auth.signOut()
            // After signing out, return to the login screen
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}