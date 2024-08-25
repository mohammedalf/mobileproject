package be.ap.examsapplication
data class Exam(
    val id: String = "",
    val title: String = "",
    val questions: List<Map<String, Any>> = emptyList(),
    val assignedUsers: List<String> = emptyList()
)

sealed class Question {
    abstract val type: String

    data class OpenQuestion(
        val questionText: String = "",
        val answer: String = ""
    ) : Question() {
        override val type: String = "OpenQuestion"
    }

    data class MultipleChoiceQuestion(
        val questionText: String = "",
        val options: List<String> = listOf(),
        val correctOption: String = ""
    ) : Question() {
        override val type: String = "MultipleChoiceQuestion"
    }
}

