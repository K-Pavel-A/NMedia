package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostsBinding

typealias OnLikeListener = (Post) -> Unit
typealias OnShareListener = (Post) -> Unit

class PostsAdapter(

    private val onLikedClicked: OnLikeListener,
    private val onShareClicked: OnShareListener)
    : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onLikedClicked, onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostsBinding,
        private val onLikedClicked: OnLikeListener,
        private val onShareClicked: OnShareListener)
        : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likesButtonview.setOnClickListener {
                onLikedClicked(post)
            }
            binding.shareButtonview.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post: Post){

            this.post = post

            with(binding) {

                descriptionTextview.text = post.author
                mainTextTextview.text = post.content
                dateTextview.text = post.published
                likesTextview.text = amountFormat(post.likes)
                shareTextview.text = amountFormat(post.shareCount)
                likesButtonview.setImageResource(getLikeIconResId(post.likedByMe))
            }
        }
        private fun amountFormat(number: Int): String {
            var text = ""
            when {
                number > 1_099_999 -> text = "${number / 1000000}.${(number % 1000000) / 100000}М"
                number > 999_999 -> text = "1М"
                number > 9_999 -> text = "${number / 1000}К"
                number > 1_099 -> text = "${(number / 1000)}.${(number % 1000) / 100}К"
                number > 999 -> text = "1К"
                number >= 0 -> text = "$number"
            }
            return text
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_active_like_24dp else R.drawable.ic_likes_24dp
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }

}