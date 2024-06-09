package com.example.nasizae_edu_pulse.data.model

data class Question(
    val questionText: String? = null,
    val answers: List<String> = emptyList(),
    val correctAnswer: String? = null,
    val explanation:String?=null
)

data class Quiz(
    val questions: List<Question> = emptyList()
)

