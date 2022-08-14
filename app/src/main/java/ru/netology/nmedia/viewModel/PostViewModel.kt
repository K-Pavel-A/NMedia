package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel: ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    fun onLikedClicked(post:Post) = repository.like(post.id)
    fun onShareClicked(post:Post) = repository.share(post.id)
}