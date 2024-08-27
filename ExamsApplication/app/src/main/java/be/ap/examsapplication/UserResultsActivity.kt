package be.ap.examsapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class UserResultsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var map: GoogleMap
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_results)

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        val userName = intent.getStringExtra("userName")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        loadResultsByUser(userName)
    }

    private fun loadResultsByUser(userName: String?) {
        firestore.collection("examResults")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.toObjects(ExamResult::class.java)
                if (::map.isInitialized) {
                    resultsRecyclerView.adapter = ResultsAdapter(results, map)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading results: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Eventueel herlaad de resultaten hier als de map nu beschikbaar is
        val userName = intent.getStringExtra("userName")
        loadResultsByUser(userName)
    }

}
