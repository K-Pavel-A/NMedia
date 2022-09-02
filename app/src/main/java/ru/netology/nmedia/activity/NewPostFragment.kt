package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)


        arguments?.textArg?.let(binding.edit::setText)

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
//            if (!binding.edit.text.isNullOrBlank()){
                val content = binding.edit.text.toString()
                viewModel.changeContent(content)
                viewModel.save()
            findNavController().navigateUp()
            }

//        }
        return binding.root
    }

//    private fun onOkButtonClicked(binding: FragmentNewPostBinding) {
//        val text = binding.edit.text
//        if (!text.isNullOrBlank()) {
//            val resultBundle = Bundle(1)
//            resultBundle.putString(POST_NEW_CONTENT_EXTRA_KEY, text.toString())
//            setFragmentResult(REQUEST_KEY, resultBundle)
//        }
//        findNavController().navigateUp()
//    }

//
//    companion object {
//        const val POST_NEW_CONTENT_EXTRA_KEY = "newPostContent"
//        const val REQUEST_KEY = "requestKey"
//
//    }
    companion object{
        var Bundle.textArg: String? by StringArg
    }
}

