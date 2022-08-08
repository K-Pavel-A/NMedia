package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val newPost = Post(
            1,
            "Нетология. Университет интернет-профессий",
            "Текст Нетологии",
            "25.07.2022"
        )

        binding.render(newPost)

        binding.likesButtonview?.setOnClickListener {
            newPost.likedByMe = !newPost.likedByMe
            binding.likesButtonview.setImageResource(getLikeIconResId(newPost.likedByMe))
            binding.likesTextview.text = amountFormat(iLike(newPost))
        }

        binding.shareButtonview?.setOnClickListener {
            binding.shareTextview.text = amountFormat(share())
        }

    }

    private var shareCount: Int = 0

    private fun share(): Int{
        return shareCount++
    }

    private fun iLike(post: Post): Int{
        if (post.likedByMe) post.likes++ else post.likes--
        return post.likes
    }

    private fun amountFormat(number:Int): String{
        var text = ""
        when{
            number > 1_099_999 -> text = "${number/1000000}.${(number%1000000)/100000}М"
            number > 999_999 -> text = "1М"
            number > 9_999 -> text = "${number/1000}К"
            number > 1_099-> text = "${(number/1000)}.${(number%1000)/100}К"
            number > 999 -> text = "1К"
            number >= 0 -> text = "$number"
        }
        return text
    }

    private fun ActivityMainBinding.render(post:Post){
        descriptionTextview.text = post.author
        mainTextTextview.text = post.content
        dateTextview.text = post.published
        likesTextview.text = amountFormat(post.likes)
        shareTextview.text = amountFormat(shareCount)
        likesButtonview?.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) = if (liked) R.drawable.ic_active_like_24dp else R.drawable.ic_likes_24dp

}