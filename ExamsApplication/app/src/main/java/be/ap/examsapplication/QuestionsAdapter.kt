package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionsAdapter (private val questions: List<Question>) :
    RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return QuestionViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)
        }

        override fun getItemCount(): Int = questions.size

        class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(android.R.id.text1)

            fun bind(question: Question) {
                textView.text = when (question) {
                    is Question.OpenQuestion -> "Open: ${question.questionText}"
                    is Question.MultipleChoiceQuestion -> "MCQ: ${question.questionText}"
                }
            }
        }
}