package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent

class ResetPassViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by KoinJavaComponent.inject<FirebaseRepository>(
        FirebaseRepository::class.java
    )

    val resetPassToId = MutableLiveData<String>()


    fun resetPassword() {
        checkReset()?.let {
            ioScope {
                firebaseRepository.resetPass(it).addOnCompleteListener {
                    if (it.isSuccessful) {
                        viewStateChanged(ResetPassViewState.ResetPassSuccess)
                    } else {
                        viewStateChanged(ResetPassViewState.ResetPassFailure)
                    }
                }
            }
        }
    }

    private fun checkReset(): String? {
        return when {
            resetPassToId.value.isNullOrEmpty() -> {
                viewStateChanged(ResetPassViewState.EmptyResetId)
                null
            }
            else -> resetPassToId.value!!
        }
    }


    sealed class ResetPassViewState : ViewState {
        object EmptyResetId : ResetPassViewState()
        object ResetPassSuccess : ResetPassViewState()
        object ResetPassFailure : ResetPassViewState()
    }


}