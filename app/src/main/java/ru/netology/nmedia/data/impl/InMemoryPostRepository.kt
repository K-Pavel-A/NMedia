package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository: PostRepository {
    private var posts
        get() = checkNotNull(data.value)

    set(value){
        data.value = value
    }

    override val data: MutableLiveData<List<Post>>


    init{
        val initalPosts = List(1000) {index ->
            Post(
                id = index+1L,
                "Нетология. Университет интернет-профессий",
                "Номер поста ${index+1}",
                "25.07.2022",
                likes = 999,
                shareCount = 999999,
                likedByMe = false
            )
        }
        data = MutableLiveData(initalPosts)
    }

    override fun like(postId:Long) {
        posts = posts.map { post->
            if(post.id == postId) post.copy(likedByMe = !post.likedByMe,likes = if (!post.likedByMe) post.likes+1 else post.likes-1)
            else post
        }

    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if(post.id == postId) post.copy(shareCount = post.shareCount+1) else post
        }
    }


}