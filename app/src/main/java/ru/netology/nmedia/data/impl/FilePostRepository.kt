package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application
): PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post:: class.java).type

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
        application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use{
            it.write(gson.toJson(value))
        }
        data.value = value
    }

    override val data: MutableLiveData<List<Post>>

    init{
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()){
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
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
        const val FILE_NAME = "posts.json"
    }

}