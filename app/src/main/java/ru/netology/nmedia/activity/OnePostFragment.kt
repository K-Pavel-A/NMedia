package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.StringArg
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.viewModel.PostViewModel

class OnePostFragment: Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOnePostBinding.inflate(inflater, container, false)
        val onePostId = arguments?.textId

        val viewHolder = PostsAdapter.ViewHolder(binding.onepost, viewModel)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val thePost = posts.find {
                onePostId?.toLong() == it.id
            }
            if(thePost != null)viewHolder.bind(thePost)

            val popupMenu by lazy {
                PopupMenu(this.requireContext(), binding.onepost.optionsButtonview).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                if (thePost != null)
                                viewModel.onRemoveClicked(thePost)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                if (thePost != null) {
                                    viewModel.onEditClicked(thePost)
                                }
                                findNavController().navigate(
                                    R.id.action_onePostFragment_to_newPostFragment,
                                    Bundle().apply {
                                        if (thePost != null) {
                                            textArg = thePost.content
                                        }
                                    })
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            binding.onepost.optionsButtonview.setOnClickListener { popupMenu.show() }
            viewModel.shareEvent.observe(viewLifecycleOwner) { post ->
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"

                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val shareIntent = Intent.createChooser(intent, "Поделиться")
                startActivity(shareIntent)
            }

            viewModel.videoURL.observe(viewLifecycleOwner) { videoURL ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
                startActivity(intent)
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.textId: String? by StringArg
    }
}