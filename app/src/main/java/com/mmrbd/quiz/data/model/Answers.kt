package com.mmrbd.quiz.data.model

data class Answers(
    val A: String?,
    val B: String?,
    val C: String?,
    val D: String?,
    var selectedOption: Map<String, String>?,
    var selectedPosition: Int?
)