package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.databinding.ActivityMainBinding
import com.example.socialapp.models.Post
import com.example.socialapp.models.PostDao
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fab.setOnClickListener{
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
        //startActivity(Intent(this, CreatePostActivity::class.java))
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val postCollection = PostDao().postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
        adapter = PostAdapter(recyclerViewOptions) { post -> onLikeClicked(post) }
        binding.recyclerviewLayout.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewLayout.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    fun onLikeClicked(post: Post) {
        PostDao().likePost(post)
    }
}