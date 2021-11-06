package com.example.toycamping.data.source.remote

import android.net.Uri
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.SnapItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FirebaseRemoteDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val fireStorage: FirebaseStorage
) :
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


    override suspend fun delete(): Task<Void>? = withContext(Dispatchers.IO) {
        return@withContext firebaseAuth.currentUser?.delete()
    }

    override suspend fun resetPass(resetPassToId: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.sendPasswordResetEmail(resetPassToId)
        }


    override suspend fun createUserBookmarkDB(id: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext fireStore.collection(id).document("camping")
                .set(emptyMap<String, CampingItem>())
        }

    override suspend fun createUserSnapDB(id: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext fireStore.collection(id).document("snap")
                .set(emptyMap<String, SnapItem>())
        }

    override suspend fun addBookmarkItem(id: String, campingItem: CampingItem): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext fireStore.collection(id).document("camping")
                .update("like", FieldValue.arrayUnion(campingItem))
        }

    override suspend fun deleteBookmarkItem(id: String, campingItem: CampingItem): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext fireStore.collection(id).document("camping")
                .update("like", FieldValue.arrayRemove(campingItem))
        }


    override suspend fun addSnapItem(id: String, uri: Uri, snapItem: SnapItem): UploadTask =
        withContext(Dispatchers.IO) {
            return@withContext fireStorage.reference.child(id).child(snapItem.name!!)
                .putFile(uri)
        }

    override suspend fun deleteSnapItem(id: String, snapItem: SnapItem): Task<Void> =
        withContext(Dispatchers.IO) {
            fireStore.collection(id).document("snap")
                .update("item", FieldValue.arrayRemove(snapItem))
            return@withContext fireStorage.reference.child(id).child(snapItem.name!!).delete()
        }


    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseAuth

    override fun getFirebaseFireStore(): FirebaseFirestore =
        fireStore

    override fun getFirebaseStorage(): FirebaseStorage =
        fireStorage
}