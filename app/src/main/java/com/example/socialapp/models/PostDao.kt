package com.example.socialapp.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = FirebaseAuth.getInstance()

    fun addPost(text: String){
        val userId = auth.currentUser!!.uid
        val userTask = UserDao().getUserByID(userId)
        val currentTime = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        GlobalScope.launch(Dispatchers.IO){
            val user = userTask.await().toObject(User::class.java)
            val post = Post(text, user!!, currentTime, id)
            postCollection.document().set(post)
        }
    }

    fun likePost(post: Post){
        val userId = auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO){
            if(post.likedBy.contains(userId)){
                post.likedBy.remove(userId)
            }
            else{
                post.likedBy.add(userId)
            }
            postCollection.document(post.id).set(post)
        }
    }
}