package es.sublimestudio.kotlinquiz.models

data class Question(
    val answers: List<Answer>,
    val title: String
)