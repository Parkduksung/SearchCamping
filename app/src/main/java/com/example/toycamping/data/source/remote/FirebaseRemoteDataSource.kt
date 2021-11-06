package com.example.toycamping.data.source.remote

import android.net.Uri
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.SnapItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

interface FirebaseRemoteDataSource {

    suspend fun login(
        id: String,
        password: String
    ): Task<AuthResult>

    suspend fun logout(): Boolean

    suspend fun register(
        id: String,
        password: String
    ): Task<AuthResult>

    suspend fun delete(): Task<Void>?

    suspend fun resetPass(
        resetPassToId: String
    ): Task<Void>

    suspend fun createUserBookmarkDB(
        id: String
    ): Task<Void>

    suspend fun createUserSnapDB(
        id: String
    ): Task<Void>


    suspend fun addBookmarkItem(
        id: String,
        campingItem: CampingItem
    ): Task<Void>

    suspend fun deleteBookmarkItem(
        id: String,
        campingItem: CampingItem
    ): Task<Void>

    suspend fun addSnapItem(
        id: String,
        uri : Uri ,
        snapItem: SnapItem
    ): UploadTask

    suspend fun deleteSnapItem(
        id: String,
        snapItem: SnapItem
    ): Task<Void>


    fun getFirebaseAuth(): FirebaseAuth

    fun getFirebaseFireStore(): FirebaseFirestore

    fun getFirebaseStorage(): FirebaseStorage
}