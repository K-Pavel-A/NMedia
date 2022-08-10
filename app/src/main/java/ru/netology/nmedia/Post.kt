package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int,
    val likedByMe: Boolean = false,
    val shareCount: Int
)
