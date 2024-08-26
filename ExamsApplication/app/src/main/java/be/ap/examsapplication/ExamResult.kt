package be.ap.examsapplication

import java.util.Date

data class ExamResult(
    val userName: String = "",
    val examTitle: String = "",
    val date: Date = Date(),
    val duration: Long = 0L,
    val score: Double = 0.0,
    val answers: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null
)
