package com.example.socialapp.models

data class Post(val text: String = "",
                val createdBy: User = User(),
                val createdAt: Long = 0L,
                val id: String= "",
                val likedBy: ArrayList<String> = ArrayList()
)
