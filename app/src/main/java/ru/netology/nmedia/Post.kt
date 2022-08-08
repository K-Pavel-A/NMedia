package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    var likedByMe: Boolean = false
)
