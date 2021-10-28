package com.example.toycamping.data.repo

import com.example.toycamping.data.source.remote.FirebaseRemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class FirebaseRepositoryImpl : FirebaseRepository {

    private val firebaseRemoteDataSource by inject<FirebaseRemoteDataSource>(
        FirebaseRemoteDataSource::class.java
    )

    override suspend fun login(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.login(id, password)
        }

    override suspend fun logout(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.logout()
        }

    override suspend fun register(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.register(id, password)
        }

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseRemoteDataSource.getFirebaseAuth()


    override suspend fun delete(): Task<Void>? = withContext(Dispatchers.IO) {
        return@withContext firebaseRemoteDataSource.delete()
    }
}