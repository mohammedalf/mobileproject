package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamsSelectionAdapter(
    private val exams: List<Exam>,
    private val onExamClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamsSelectionAdapter.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ExamViewHolder(view, onExamClick)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.bind(exam)
    }

    override fun getItemCount(): Int = exams.size

    class ExamViewHolder(itemView: View, private val onExamClick: (Exam) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val examTitleTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(exam: Exam) {
            examTitleTextView.text = exam.title
            itemView.setOnClickListener {
                onExamClick(exam)
            }
        }
    }
}
