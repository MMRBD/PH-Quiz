package com.mmrbd.quiz.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmrbd.quiz.utils.network.Result
import com.mmrbd.quiz.data.model.QuestionResponse
import com.mmrbd.quiz.data.model.Question
import com.mmrbd.quiz.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val questionResponseSFlow = MutableSharedFlow<Result<QuestionResponse>>()
    val questionFlow = MutableSharedFlow<Question>()

    init {
        getQuestions()
    }

    private fun getQuestions() {
        viewModelScope.launch {
            repository.geQuestions().collect {
                questionResponseSFlow.emit(
                    it
                )
            }
        }
    }

    fun setQuestionX(question: Question, position: Int) {


        viewModelScope.launch {
            if (position != 0)
                delay(2000)
            questionFlow.emit(question)
        }
    }
}