package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.StringArg
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment: Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.textArg.let(binding.edit:: setText)

        binding.edit.requestFocus()
        binding.edit.showKeyboard()

        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()){
                viewModel.onSaveButtonClicked(binding.edit.text.toString())
            }
            findNavController().navigateUp()
        }

        return binding.root
    }
    companion object{
        var Bundle.textArg: String? by StringArg
    }
}


