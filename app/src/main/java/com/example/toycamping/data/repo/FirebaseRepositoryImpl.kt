package com.example.toycamping.data.repo

import android.net.Uri
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.data.source.remote.FirebaseRemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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

    override suspend fun delete(): Task<Void>? = withContext(Dispatchers.IO) {
        return@withContext firebaseRemoteDataSource.delete()
    }

    override suspend fun createUserBookmarkDB(id: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.createUserBookmarkDB(id)
        }

    override suspend fun createUserSnapDB(id: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.createUserSnapDB(id)
        }

    override suspend fun resetPass(resetPassToId: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.resetPass(resetPassToId)
        }

    override suspend fun addBookmarkItem(id: String, campingItem: CampingItem): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.addBookmarkItem(id, campingItem)
        }

    override suspend fun deleteBookmarkItem(id: String, campingItem: CampingItem): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.deleteBookmarkItem(id, campingItem)
        }

    override suspend fun addSnapItem(id: String, uri: Uri, snapItem: SnapItem): UploadTask =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.addSnapItem(id, uri, snapItem)
        }

    override suspend fun deleteSnapItem(id: String, snapItem: SnapItem): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseRemoteDataSource.deleteSnapItem(id, snapItem)
        }

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseRemoteDataSource.getFirebaseAuth()

    override fun getFirebaseFireStore(): FirebaseFirestore =
        firebaseRemoteDataSource.getFirebaseFireStore()

    override fun getFirebaseStorage(): FirebaseStorage =
        firebaseRemoteDataSource.getFirebaseStorage()

}