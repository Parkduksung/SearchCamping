package com.example.toycamping.data.repo

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

interface FirebaseRepository {

    suspend fun login(
        id: String,
        password: String
    ): Task<AuthResult>


    suspend fun logout(): Boolean

    suspend fun register(
        id: String,
        password: String
    ): Task<AuthResult>

    fun getFirebaseAuth(): FirebaseAuth

}