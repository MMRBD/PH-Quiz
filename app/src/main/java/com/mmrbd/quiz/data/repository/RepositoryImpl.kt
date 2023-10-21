package com.mmrbd.quiz.data.repository

import com.mmrbd.quiz.data.api.ApiService
import com.mmrbd.quiz.utils.network.Failure
import com.mmrbd.quiz.utils.network.Result
import com.mmrbd.quiz.utils.network.getErrorTypeByHTTPCode
import com.mmrbd.quiz.data.model.QuestionResponse
import com.mmrbd.quiz.utils.AppLogger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.net.UnknownHostException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override fun geQuestions(): Flow<Result<QuestionResponse>> = callbackFlow {
        trySend(Result.Loading())

        try {
            val result = apiService.getQuestions()
            if (result.isSuccessful) {
                AppLogger.log("geQuestions:: $result")

                trySend(Result.Success(result.body()!!))

            } else {
                trySend(
                    Result.Error(
                    getErrorTypeByHTTPCode(result.code()), null
                ))
            }
        } catch (exception: Throwable) {
            when (exception) {
                is UnknownHostException -> {
                    trySend(Result.Error((Failure.HTTP.NetworkConnection)))
                }

                else -> {
                    trySend(Result.Error(Failure.Exception(exception), null))
                }
            }
        }

        awaitClose()

    }
}