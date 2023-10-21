package com.mmrbd.quiz.data.api

import com.mmrbd.quiz.data.model.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("quiz.json")
    suspend fun getQuestions(): Response<QuestionResponse>
}