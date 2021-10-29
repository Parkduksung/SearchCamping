package com.example.toycamping.data.source.remote

import com.example.toycamping.data.model.CampingItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRemoteDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
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

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseAuth

    override fun getFirebaseFireStore(): FirebaseFirestore =
        fireStore
}