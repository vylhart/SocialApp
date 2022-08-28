package com.example.socialapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.Utils.Companion.getTimeAgo
import com.example.socialapp.databinding.ItemPostBinding
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val onLikeClicked: (Post)-> Unit) :
    FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, onLikeClicked)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.bindTo(getItem(position))
    }

}

class PostViewHolder(private val binding: ItemPostBinding,private val onLikeClicked: (Post) -> Unit) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bindTo(post: Post) {
        with(binding){
            postTitle.text = post.text
            userName.text = post.createdBy.userName
            likeCount.text = post.likedBy.size.toString()
            createdAt.text = getTimeAgo(post.createdAt)
            likeButton.setOnClickListener { onLikeClicked(post) }
            Glide.with(binding.userImage.context)
                .load(post.createdBy.imageUrl)
                .circleCrop()
                .into(userImage)
        }

    }
}
