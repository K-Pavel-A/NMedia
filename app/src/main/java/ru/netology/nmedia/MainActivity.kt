package ru.netology.nmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.activity.NewPostActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(
            viewModel
        )
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.addButton.setOnClickListener {
            with (binding.contentEditText){
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
            }
        }

        viewModel.currentPost.observe(this){ currentPost ->
            with(binding.contentEditText){
                var content = currentPost?.content
                setText(currentPost?.content)
                if (content != null) {
                    requestFocus()
                    showKeyboard()
                    with(binding.editPostGroup){
                        with(binding.editablePostText){
                            text = content
                        }
                        visibility = View.VISIBLE
                        binding.cancelUpdateButton.setOnClickListener {
                            content = null
                            text = content
                            clearFocus()
                            hideKeyboard()
                            visibility = View.INVISIBLE
                        }
                    }
                } else {
                    clearFocus()
                    hideKeyboard()
                    with(binding.editPostGroup) {
                        visibility = View.INVISIBLE
                    }
                }
            }
        }

        viewModel.shareEvent.observe(this){ post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться")
            startActivity(shareIntent)
        }

        val activityLauncher = registerForActivityResult(
            NewPostActivity.ResultContract
        ){ postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked)
        }

        binding.addButton.setOnClickListener {
            activityLauncher.launch(Unit)
        }
//
//        viewModel.editEvent.observe(this){ post ->
//            val activityLauncher = registerForActivityResult(
//                NewPostActivity.ResultContract
//            ){ postContent: String? ->
//                postContent?.let(viewModel::onSaveButtonClicked)
//            }
//            activityLauncher.launch(post?.content)
//        }

    }
}