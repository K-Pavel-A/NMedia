package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.json.JsonNull.content
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
//                viewModel.save()
            }
            findNavController().navigateUp()
        }

//
//        private fun onOkButtonClicked(postContent: String?) {
//            val intent = Intent()
//            if (postContent.isNullOrBlank()) {
//                activity?.setResult(Activity.RESULT_CANCELED, intent)
//            } else {
//                intent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
//                activity?.setResult(Activity.RESULT_OK, intent)
//            }
//            activity?.finish()
//        }
//
//        private companion object {
//            const val POST_CONTENT_EXTRA_KEY = "postContent"
//        }
//
//        object ResultContract : ActivityResultContract<Unit, String?>() {
//
//            override fun createIntent(context: Context, input: Unit): Intent =
//                Intent(context, NewPostFragment::class.java)
//
//            override fun parseResult(resultCode: Int, intent: Intent?): String? {
//                if (resultCode != Activity.RESULT_OK) return null
//                intent ?: return null
//
//                return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
//            }
//        }

        return binding.root
    }
    companion object{
        var Bundle.textArg: String? by StringArg
    }
}


