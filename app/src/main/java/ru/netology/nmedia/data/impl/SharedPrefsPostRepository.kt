package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import androidx.core.content.edit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.Delegates

class SharedPrefsPostRepository(
    application: Application
): PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ){_, _, newValue ->
        prefs.edit(){ putLong(NEXT_ID_PREFS_KEY, newValue)}
    }
//    = GENERATED_POSTS_AMOUNT.toLong()

    init {

    }

    private var posts
        get() = checkNotNull(data.value)

    set(value){
        prefs.edit{
            val serializedPosts = Json.encodeToString(value)
            putString(POSTS_PREFS_KEY, serializedPosts)
        }
        data.value = value
    }

    override val data: MutableLiveData<List<Post>>


//    init{
//        val initalPosts = List(GENERATED_POSTS_AMOUNT) { index ->
//            Post(
//                id = index+1L,
//                "Нетология. Университет интернет-профессий",
//                "Номер поста ${index+1}",
//                "25.07.2022",
//            )
//        }
//        data = MutableLiveData(initalPosts)
//    }

    init{
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null ){
             Json.decodeFromString (serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
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
        posts = posts.map{
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId))+ posts
    }

    private companion object{
//        const val GENERATED_POSTS_AMOUNT = 1000
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "posts"
    }

}