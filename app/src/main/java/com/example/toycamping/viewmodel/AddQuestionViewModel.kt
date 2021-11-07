package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.QuestionItem
import java.text.SimpleDateFormat
import java.util.*

class AddQuestionViewModel(app: Application) : BaseViewModel(app) {

    fun addQuestion(detail: String) {
        checkAddQuestion(detail)?.let {
            viewStateChanged(AddQuestionViewState.AddQuestion(it))
        }
    }

    private fun checkAddQuestion(detail: String): QuestionItem? {
        return when {
            detail.isNullOrEmpty() -> {
                viewStateChanged(AddQuestionViewState.Error("내용을 입력하세요."))
                null
            }
            else -> {
                val date = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                val questionItem = QuestionItem(date = date, detail = detail)
                questionItem
            }
        }
    }

    sealed class AddQuestionViewState : ViewState {
        data class AddQuestion(val item: QuestionItem) : AddQuestionViewState()
        data class Error(val message: String) : AddQuestionViewState()
    }
}