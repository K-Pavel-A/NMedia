package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository: PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private var posts
        get() = checkNotNull(data.value)

    set(value){
        data.value = value
    }

    override val data: MutableLiveData<List<Post>>


    init{
        val initalPosts = List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index+1L,
                "Нетология. Университет интернет-профессий",
                "Номер поста ${index+1}",
                "25.07.2022",
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

    override fun delete(postId: Long) {
        posts = posts.filter { post->
            post.id != postId
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map{
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId))+ posts
    }

    private companion object{
        const val GENERATED_POSTS_AMOUNT = 1000
    }

}