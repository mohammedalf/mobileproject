package be.ap.examsapplication

data class Exam(
    val id: String = "",
    val title: String = "",
    val questions: List<Question> = listOf()
)

sealed class Question {
    data class OpenQuestion(
        val questionText: String = "",
        val answer: String = ""
    ) : Question()

    data class MultipleChoiceQuestion(
        val questionText: String = "",
        val options: List<String> = listOf(),
        val correctOption: String = ""
    ) : Question()
}
