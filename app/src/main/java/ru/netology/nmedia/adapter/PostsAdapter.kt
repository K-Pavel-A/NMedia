package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostsBinding



class PostsAdapter(
    private val interactionListener: PostInteractionListener
    )
    : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(

        private val binding: PostsBinding,
        listener: PostInteractionListener,
        )
        : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.optionsButtonview).apply{
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem->
                    when (menuItem.itemId){
                        R.id.remove ->{
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likesButtonview.setOnClickListener {
                listener.onLikedClicked(post)
            }
            binding.shareButtonview.setOnClickListener {
                listener.onShareClicked(post)
            }
            binding.optionsButtonview.setOnClickListener { popupMenu.show() }
            binding.videoImage.setOnClickListener{
                listener.onPlayVideoClicked(post)
            }
            binding.playvideoButton.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }
            binding.emptyPartPost.setOnClickListener{
                listener.onPostClicked(post)
            }
        }

        fun bind(post: Post){

            this.post = post

            with(binding) {
                descriptionTextview.text = post.author
                mainTextTextview.text = post.content
                dateTextview.text = post.published
                likesButtonview.text = amountFormat(post.likes)
                likesButtonview.isChecked = post.likedByMe
                shareButtonview.text = amountFormat(post.shareCount)
                if (post.video.isBlank()) {
                    videoGroup.visibility = View.GONE
                } else {
                    videoGroup.visibility = View.VISIBLE
                    linkvideoTextview.text = post.video
                }
            }
        }
        private fun amountFormat(number: Int): String {
            var text = ""
            when {
                number > 1_099_999 -> text = "${number / 1000000}.${(number % 1000000) / 100000}??"
                number > 999_999 -> text = "1??"
                number > 9_999 -> text = "${number / 1000}??"
                number > 1_099 -> text = "${(number / 1000)}.${(number % 1000) / 100}??"
                number > 999 -> text = "1??"
                number >= 0 -> text = "$number"
            }
            return text
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}