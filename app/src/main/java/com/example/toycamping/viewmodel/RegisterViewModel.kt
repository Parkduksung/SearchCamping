package com.example.toycamping.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent.inject

class RegisterViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    val registerId = MutableLiveData<String>()
    val registerPass = MutableLiveData<String>()

    fun register() {
        checkRegister()?.let {
            ioScope {
                firebaseRepository.register(it.userId, it.userPass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        viewStateChanged(RegisterViewState.RegisterSuccess)
                    } else {
                        Log.d("결과", it.exception?.message.toString())
                        viewStateChanged(RegisterViewState.RegisterFailure)
                    }
                }
            }
        }
    }

    private fun checkRegister(): User? {
        return when {
            registerId.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.EmptyUserId)
                null
            }
            registerPass.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.EmptyUserPass)
                null
            }
            else -> {
                User(registerId.value!!, registerPass.value!!)
            }
        }
    }

    private data class User(val userId: String, val userPass: String)

    sealed class RegisterViewState : ViewState {
        object EmptyUserId : RegisterViewState()
        object EmptyUserPass : RegisterViewState()
        object RegisterSuccess : RegisterViewState()
        object RegisterFailure : RegisterViewState()
        object RouteMyPage : RegisterViewState()
    }

}