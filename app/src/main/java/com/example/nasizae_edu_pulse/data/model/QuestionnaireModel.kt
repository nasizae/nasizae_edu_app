package com.example.nasizae_edu_pulse.data.model

data class QuestionnaireModel(
val questionText: String? = null,
val answers: List<String> = emptyList(),
)

data class QuizQuestionnaire(
    val questions: List<QuestionnaireModel> = emptyList()
)

