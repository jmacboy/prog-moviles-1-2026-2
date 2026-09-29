package com.example.practicainstagrammvvm

import androidx.lifecycle.ViewModel
import com.example.practicainstagram.Comment
import com.example.practicainstagram.Post
import com.example.practicainstagram.Story
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    fun likePost(post: Post) {
        val posts = _uiState.value.posts
        val index = posts.indexOfFirst { it.id == post.id }
        val updatedPost = post.copy(isLiked = !post.isLiked)
        val updatedPosts = posts.toMutableList()
        updatedPosts[index] = updatedPost
        _uiState.update {
            it.copy(
                posts = updatedPosts,
                showLikedPost = updatedPost.isLiked,
                showDislikedPost = !updatedPost.isLiked
            )
        }
    }

    fun postCommentToggle(post: Post) {
        val posts = _uiState.value.posts
        val index = posts.indexOfFirst { it.id == post.id }
        val updatedPost = post.copy(showComment = !post.showComment)
        val updatedPosts = posts.toMutableList()
        updatedPosts[index] = updatedPost
        _uiState.update {
            it.copy(posts = updatedPosts)
        }
    }

    fun submitComment(post: Post, text: String) {
        val posts = _uiState.value.posts
        val index = posts.indexOfFirst { it.id == post.id }
        val comments = post.comments.toMutableList() as ArrayList<Comment>
        comments.add(Comment("user1", text))
        val updatedPost = post.copy(comments = comments)
        val updatedPosts = posts.toMutableList()
        updatedPosts[index] = updatedPost
        _uiState.update {
            it.copy(posts = updatedPosts)
        }
    }

    private val _uiState = MutableStateFlow(InstagramUiState())
    val uiState: StateFlow<InstagramUiState> = _uiState.asStateFlow()

}

data class InstagramUiState(
    val stories: List<Story> = arrayListOf(
        Story(R.drawable.profile, "user1"),
        Story(R.drawable.profile, "user2"),
        Story(R.drawable.profile, "user3"),
        Story(R.drawable.profile, "user4"),
        Story(R.drawable.profile, "user5"),
        Story(R.drawable.profile, "user6"),
    ),
    val posts: List<Post> = arrayListOf(
        Post(
            1,
            R.drawable.profile,
            "juanito",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "1 de enero",
            "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            comments = arrayListOf(
                Comment("user1", "Me gusta tu publicación")
            ),
        ),
        Post(
            2,
            R.drawable.profile,
            "pepito",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "12 de noviembre",
            "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        ),
        Post(3,
            R.drawable.profile,
            "pedrito",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "1 de enero",
            "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            comments = arrayListOf(
                Comment("user1", "Cualquier cosa"),
                Comment("user2", "Ya mejor que le corten el internet")
            ),
        ),
        Post(4,
            R.drawable.profile,
            "user2",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "2 de febrero",
            "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        ),
        Post(5,
            R.drawable.profile,
            "user1",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "1 de enero",
            "Esta es una descripción de la publicación 1"
        ),
        Post(6,
            R.drawable.profile,
            "user2",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "2 de marzo",
            "Esta es una descripción de la publicación 2"
        ),
    ),
    val showLikedPost: Boolean = false,
    val showDislikedPost: Boolean = false,
    val commentText : String = ""
)