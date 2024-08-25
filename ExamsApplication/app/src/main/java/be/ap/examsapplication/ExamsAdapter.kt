package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamsAdapter(
    private var exams: List<Exam>,
    private val onExamClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamsAdapter.ExamViewHolder>() {

    fun setExams(exams: List<Exam>) {
        this.exams = exams
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.bind(exam, onExamClick)
    }

    override fun getItemCount(): Int = exams.size

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val examTitleTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(exam: Exam, onExamClick: (Exam) -> Unit) {
            examTitleTextView.text = exam.title
            itemView.setOnClickListener { onExamClick(exam) }
        }
    }
}
