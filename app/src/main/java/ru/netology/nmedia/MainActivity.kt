package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostsBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(
            onLikedClicked = { post ->
                viewModel.onLikedClicked(post)
            },
            onShareClicked = { post ->
                viewModel.onShareClicked(post)
            }
        )
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)

//            posts.map { post ->
//                PostsBinding.inflate(layoutInflater, binding.container, false
//                ).apply {
//                    descriptionTextview.text = post.author
//                    mainTextTextview.text = post.content
//                    dateTextview.text = post.published
//                    likesTextview.text = amountFormat(post.likes)
//                    shareTextview.text = amountFormat(post.shareCount)
//                    likesButtonview.setImageResource(getLikeIconResId(post.likedByMe))
//
//                    likesButtonview.setOnClickListener {
//                        viewModel.onLikedClicked(post)
//                    }
//
//                    shareButtonview.setOnClickListener {
//                        viewModel.onShareClicked(post)
//                    }
//                }.root
//            }.forEach { postView ->
//                binding.container.addView(postView)
//
//            }
        }
    }

//    private fun amountFormat(number: Int): String {
//        var text = ""
//        when {
//            number > 1_099_999 -> text = "${number / 1000000}.${(number % 1000000) / 100000}М"
//            number > 999_999 -> text = "1М"
//            number > 9_999 -> text = "${number / 1000}К"
//            number > 1_099 -> text = "${(number / 1000)}.${(number % 1000) / 100}К"
//            number > 999 -> text = "1К"
//            number >= 0 -> text = "$number"
//        }
//        return text
//    }
//
//    @DrawableRes
//    private fun getLikeIconResId(liked: Boolean) =
//        if (liked) R.drawable.ic_active_like_24dp else R.drawable.ic_likes_24dp

}