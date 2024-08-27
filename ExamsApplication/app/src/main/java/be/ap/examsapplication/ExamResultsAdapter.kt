package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamResultsAdapter(
    private val results: List<ExamResult>
) : RecyclerView.Adapter<ExamResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exams, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int = results.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        fun bind(result: ExamResult) {
            userNameTextView.text = "User: ${result.userName}"
            scoreTextView.text = "Score: ${result.score}/20"
            addressTextView.text = "Address: ${result.address ?: "N/A"}"
            durationTextView.text = "Duration: ${result.duration} seconds"
        }
    }
}
