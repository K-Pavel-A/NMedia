package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository: PostRepository {

    override val data = MutableLiveData<Post>(
        Post(
            1,
            "Нетология. Университет интернет-профессий",
            "Текст Нетологии",
            "25.07.2022",
            likes = 0,
            shareCount = 0
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value){
            "Data value should not be null"
        }
        val likedPost = currentPost.copy(likedByMe = !currentPost.likedByMe,likes = if (!currentPost.likedByMe) currentPost.likes+1 else currentPost.likes-1)
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value){
            "Data value should not be null"
        }
        val sharedPost = currentPost.copy(shareCount = currentPost.shareCount+1)
        data.value = sharedPost
    }


}