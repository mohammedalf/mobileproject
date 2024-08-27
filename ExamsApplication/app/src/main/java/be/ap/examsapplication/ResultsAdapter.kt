package be.ap.examsapplication

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Locale

class ResultsAdapter(
    private val results: List<ExamResult>,
    private val map: GoogleMap
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exam_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
        result.latitude?.let { lat ->
            result.longitude?.let { lon ->
                val location = LatLng(lat, lon)
                map.addMarker(MarkerOptions().position(location).title("Exam Location: ${result.examTitle}"))
            }
        }
    }



    override fun getItemCount(): Int = results.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val examTitleTextView: TextView = itemView.findViewById(R.id.examTitleTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
        private val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)

        fun bind(result: ExamResult) {
            userNameTextView.text = "User: ${result.userName}"
            examTitleTextView.text = "Exam: ${result.examTitle}"
            dateTextView.text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(result.date)}"
            durationTextView.text = "Duration: ${result.duration} seconds"
            scoreTextView.text = "Score: ${result.score}/20"
            addressTextView.text = "Address: ${result.address ?: "N/A"}"
            latitudeTextView.text = "Latitude: ${result.latitude ?: "N/A"}"
            longitudeTextView.text = "Longitude: ${result.longitude ?: "N/A"}"
        }
    }
}

/*class ResultsAdapter(
    private val results: List<ExamResult>,
    private val map: GoogleMap
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exam_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
        result.latitude?.let { lat ->
            result.longitude?.let { lon ->
                val location = LatLng(lat, lon)
                map.addMarker(MarkerOptions().position(location).title("Exam Location: ${result.examTitle}"))
            }
        }
    }

    override fun getItemCount(): Int = results.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val examTitleTextView: TextView = itemView.findViewById(R.id.examTitleTextView)
//        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
//        private val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
//        private val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)

        fun bind(result: ExamResult) {
            userNameTextView.text = "User: ${result.userName}"
            examTitleTextView.text = "Exam: ${result.examTitle}"
//            dateTextView.text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(result.date)}"
            durationTextView.text = "Duration: ${result.duration} seconds"
            scoreTextView.text = "Score: ${result.score}/20"
            addressTextView.text = "Address: ${result.address ?: "N/A"}"
//            latitudeTextView.text = "Latitude: ${result.latitude ?: "N/A"}"
//            longitudeTextView.text = "Longitude: ${result.longitude ?: "N/A"}"
        }
    }
}*/


