package com.example.socialapp.models

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()
    val userCollection= db.collection("users")

    fun addUser(firebaseUser: FirebaseUser){
        val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
        GlobalScope.launch(Dispatchers.IO) {
            userCollection.document(user.uid).set(user)
        }
    }

    fun getUserByID(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }

}