package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
//import ru.netology.nmedia.activity.EditPostActivity
//import ru.netology.nmedia.activity.NewPostFragment
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(
            viewModel
        )

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.shareEvent.observe(viewLifecycleOwner){ post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val shareIntent = Intent.createChooser(intent, "Поделиться")
            startActivity(shareIntent)
        }

//        val newPostActivityLauncher = registerForActivityResult(
//            NewPostFragment.ResultContract
//        ){ postContent: String? ->
//            postContent?.let(viewModel::onSaveButtonClicked)
//        }
//
//        val editPostActivityLauncher = registerForActivityResult(
//            EditPostActivity.ResultContract
//        ){ postContent: String? ->
//            postContent?.let(viewModel::onSaveButtonClicked)
//        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }


        viewModel.editEvent.observe(viewLifecycleOwner) { currentPost ->
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { textArg = currentPost?.content }
            )
        }

        viewModel.videoURL.observe(viewLifecycleOwner) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        }


        return binding.root
    }
}
