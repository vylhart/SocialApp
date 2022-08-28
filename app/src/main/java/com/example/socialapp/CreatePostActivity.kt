package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.socialapp.databinding.ActivityCreatePostBinding
import com.example.socialapp.models.PostDao

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            val text = (it as Button).text.toString().trim()
            if(!text.isEmpty()){
                PostDao().addPost(text)
                finish()
            }
        }
    }

}