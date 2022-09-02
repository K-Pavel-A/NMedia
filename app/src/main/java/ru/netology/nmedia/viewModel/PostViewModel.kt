package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository

class PostViewModel(
    application: Application
): AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository =
       FilePostRepository(application)

    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)
    val shareEvent = SingleLiveEvent<Post>()
    val editEvent = SingleLiveEvent<Post?>()
    val videoURL = SingleLiveEvent<String?>()
//    val navigateToPostEvent = SingleLiveEvent<Long>()

    private val empty = Post(
        id = 0,
        content = "",
        author = "",
        likedByMe = false,
        likes = 0,
        published = ""
    )

    override fun onLikedClicked(post:Post) = repository.like(post.id)
    override fun onShareClicked(post:Post){
        repository.share(post.id)
        shareEvent.value = post
    }
    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editEvent.value = post
    }

    override fun onPlayVideoClicked(post: Post) {
        videoURL.value = post.video
    }

    fun onSaveButtonClicked(content:String){
        if (content.isBlank()) return
        val post = currentPost.value?.copy(content = content) ?: Post (
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "25.09.22"
                )
        repository.save(post)
        currentPost.value = null
    }
//
//    override fun onPostClicked(post: Post) {
//        currentPost.value = post
//        navigateToPostEvent.value = post.id
//    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (currentPost.value?.content == text) {
            return
        }
        currentPost.value = currentPost.value?.copy(content = text)
    }

    fun save() {
        currentPost.value?.let {
            repository.save(it)
        }
        currentPost.value = empty
    }

}