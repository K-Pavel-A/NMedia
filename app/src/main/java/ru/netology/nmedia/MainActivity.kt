package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.activity.EditPostActivity
import ru.netology.nmedia.activity.NewPostActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
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

        viewModel.shareEvent.observe(this){ post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться")
            startActivity(shareIntent)
        }

        val newPostActivityLauncher = registerForActivityResult(
            NewPostActivity.ResultContract
        ){ postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked)
        }

        val editPostActivityLauncher = registerForActivityResult(
            EditPostActivity.ResultContract
        ){ postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked)
        }

        binding.addButton.setOnClickListener {
            newPostActivityLauncher.launch(Unit)
        }

        viewModel.editEvent.observe(this) { currentPost ->
            editPostActivityLauncher.launch(currentPost?.content)
        }

        viewModel.videoURL.observe(this) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        }
    }
    }
