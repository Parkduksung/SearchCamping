package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel(app: Application) : BaseViewModel(app) {

    val userIdLiveData = MutableLiveData<String>()
    val userPassLiveData = MutableLiveData<String>()

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)


    fun login() {
        checkLogin()?.let {
            ioScope {
                firebaseRepository.login(it.userId, it.userPass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewStateChanged(LoginViewState.LoginSuccess)
                    } else {
                        viewStateChanged(LoginViewState.LoginFailure)
                    }
                }
            }
        }
    }


    fun routeRegister() {
        viewStateChanged(LoginViewState.RouteRegister)
    }

    fun routeResetPassword() {
        viewStateChanged(LoginViewState.RouteResetPassword)
    }

    private fun checkLogin(): User? {
        return when {
            userIdLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.EmptyUserId)
                null
            }
            userPassLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.EmptyUserPass)
                null
            }
            else -> {
                User(userIdLiveData.value!!, userPassLiveData.value!!)
            }
        }
    }

    private data class User(val userId: String, val userPass: String)


    sealed class LoginViewState : ViewState {
        object EmptyUserId : LoginViewState()
        object EmptyUserPass : LoginViewState()
        object LoginSuccess : LoginViewState()
        object LoginFailure : LoginViewState()
        object RouteRegister : LoginViewState()
        object RouteResetPassword : LoginViewState()
    }

}
