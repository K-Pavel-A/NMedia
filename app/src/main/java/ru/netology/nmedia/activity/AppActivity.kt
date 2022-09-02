package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, "Content can't be empty", LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok){
                        finish()
                    }
                    .show()
                return@let
            }

         val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        fragment.navController.navigate(
            R.id.action_feedFragment_to_newPostFragment,
            Bundle().apply { textArg = text }
        )
//        findNavController(R.id.nav_host_fragment).navigate
////
//            intent.removeExtra(Intent.EXTRA_TEXT)
//            findNavController(R.id.nav_host_fragment).navigate(
//                R.id.action_feedFragment_to_newPostFragment,
//                Bundle().apply {
//                    textArg = text
//                }
//            )
        }
    }
}