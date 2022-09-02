package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.FragmentNewPostBinding


class EditPostActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.requestFocus()
        binding.edit.setText(intent.getStringExtra(Intent.EXTRA_TEXT))

        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text.toString()
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                intent.putExtra(POST_NEW_CONTENT_EXTRA_KEY, text)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private companion object{
        const val POST_NEW_CONTENT_EXTRA_KEY = "newPostContent"
    }

    object ResultContract: ActivityResultContract<String?, String?>(){

        override fun createIntent(context: Context, input: String?): Intent =
            Intent(context, EditPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)


        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_NEW_CONTENT_EXTRA_KEY)
        }
    }

}