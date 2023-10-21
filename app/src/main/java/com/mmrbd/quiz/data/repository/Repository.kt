package com.mmrbd.quiz.data.repository

import com.mmrbd.quiz.utils.network.Result
import com.mmrbd.quiz.data.model.QuestionResponse
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun geQuestions(): Flow<Result<QuestionResponse>>
}