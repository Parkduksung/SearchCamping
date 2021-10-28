package com.example.toycamping.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRemoteDataSourceImpl(private val firebaseAuth: FirebaseAuth) :
    FirebaseRemoteDataSource {


    override suspend fun login(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.signInWithEmailAndPassword(id, password)
        }

    override suspend fun logout(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext try {
                firebaseAuth.signOut()
                firebaseAuth.currentUser == null
            } catch (e: Exception) {
                false
            }
        }

    override suspend fun register(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.createUserWithEmailAndPassword(
                id,
                password
            )
        }

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseAuth

}