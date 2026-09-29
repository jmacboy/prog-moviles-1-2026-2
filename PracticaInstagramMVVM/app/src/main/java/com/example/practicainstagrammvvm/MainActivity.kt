package com.example.practicainstagrammvvm

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practicainstagram.Comment
import com.example.practicainstagram.Post
import com.example.practicainstagram.Story
import com.example.practicainstagrammvvm.ui.theme.PracticaInstagramMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaInstagramMVVMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun shareText(context: Context, text: String) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.showLikedPost) {
        if (uiState.showLikedPost) {
            Toast.makeText(
                context,
                "Has dado like a la publicación",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(uiState.showDislikedPost) {
        if (uiState.showDislikedPost) {
            Toast.makeText(
                context,
                "Has quitado el like a la publicación",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Column(modifier = modifier) {
        Header()
        StoryPanel(uiState.stories)
        PostList(vm)
        Footer()
    }
}

@Composable
fun Footer() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            contentDescription = "Home Icon",
            painter = painterResource(id = R.drawable.ic_home),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .size(24.dp)
        )
        Icon(
            contentDescription = "Search Icon",
            painter = painterResource(id = R.drawable.ic_search),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .size(24.dp)
        )
        Icon(
            contentDescription = "Reels Icon",
            painter = painterResource(id = R.drawable.ic_reels),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .size(24.dp)
        )
        Icon(
            contentDescription = "Shop Icon",
            painter = painterResource(id = R.drawable.ic_shop),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .size(24.dp)
        )
        Icon(
            contentDescription = "Profile Icon",
            painter = painterResource(id = R.drawable.ic_account),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun ColumnScope.PostList(vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(uiState.posts) { post ->
            PostItem(
                post, vm
            )
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    vm: MainViewModel = viewModel(),
) {
    Column {
        PostHeader(post)
        PostImage(post)
        PostActions(post, vm)
        PostReactions(post)
        PostDescription(post)
        PostDate(post)
        PostComments(post, vm)
    }
}

@Composable
fun PostComments(post: Post, vm: MainViewModel = viewModel()) {
    if (post.showComment) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(post.comments.size * 50.dp + 60.dp)
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        ) {
            CommentList(post.comments)
            CommentBox(post, vm)
        }
    }
}

@Composable
fun ColumnScope.CommentList(comments: ArrayList<Comment>) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .fillMaxWidth()
            .weight(1f)
    ) {
        items(comments) {
            CommentItem(comment = it)
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = comment.userName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .fillMaxWidth()
        )
        Text(
            text = comment.text,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun CommentBox(post: Post, vm: MainViewModel = viewModel()) {
    val commentText = rememberSaveable { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)
    ) {
        TextField(
            value = commentText.value,
            onValueChange = { commentText.value = it },
            placeholder = { Text(text = "Add a comment...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Gray,
                errorContainerColor = Color.Red,
            ),
            modifier = Modifier
                .padding(8.dp, 0.dp)
                .weight(1f)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier

                .background(Color(0xFF0095F6), shape = CircleShape)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_up),
                contentDescription = "Send Icon",
                modifier = Modifier
                    .size(32.dp)
                    .padding(8.dp)
                    .clickable {
                        if (commentText.value.isNotBlank()) {
                            vm.submitComment(post, commentText.value)
                            commentText.value = ""
                        }
                    }
            )
        }

    }
}

@Composable
fun PostDescription(post: Post) {
    Text(
        text = post.description,
        fontSize = 12.sp,
        textAlign = TextAlign.Justify,
        lineHeight = 14.sp,
        modifier = Modifier
            .padding(8.dp, 2.dp, 8.dp, 0.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PostDate(post: Post) {
    Text(
        text = post.date,
        fontSize = 10.sp,
        color = Color.Gray,
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 10.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PostReactions(post: Post) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
                .width(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile1",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .zIndex(2f)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile2",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .size(20.dp)
                    .offset(x = 12.dp)
                    .clip(CircleShape)
                    .zIndex(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile3",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .size(20.dp)
                    .offset(x = 24.dp)
                    .clip(CircleShape)
            )

        }
        Text(
            text = post.reactionName + " y otros",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp, 0.dp)
                .weight(1f)
        )
    }
}

@Composable
fun PostActions(
    post: Post,
    vm: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp, 0.dp)
    ) {

        Icon(
            painter = painterResource(id = if (post.isLiked) R.drawable.ic_heart_painted else R.drawable.ic_heart),
            contentDescription = "Like Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .clickable {
                    vm.likePost(post)
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_message),
            contentDescription = "Comment Icon",
            modifier = Modifier
                .padding(6.dp)
                .size(30.dp)
                .clickable {
                    vm.postCommentToggle(post)
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "Share Icon",
            modifier = Modifier
                .padding(8.dp, 10.dp, 8.dp, 8.dp)
                .size(22.dp)
                .clickable {
                    val textToShare = "Mira esta publicación de ${post.userName}: ${post.imageUrl}"
                    shareText(context, textToShare)
                }
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_bookmark),
            contentDescription = "Save Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(22.dp)
        )
    }
}

@Composable
fun PostImage(post: Post) {
    AsyncImage(
        model = post.imageUrl,
        contentDescription = "Post Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}

@Composable
fun PostHeader(post: Post) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 2.dp)
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = post.profileId),
            contentScale = ContentScale.Crop,
            contentDescription = "Profile Image",
            modifier = Modifier
                .padding(2.dp)
                .size(25.dp)
                .clip(CircleShape)
        )
        Text(
            text = post.userName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_dots),
            contentDescription = "More Options",
            modifier = Modifier
                .padding(4.dp)
                .size(15.dp)
        )
    }
}

@Composable
fun StoryPanel(stories: List<Story> = arrayListOf()) {
    val context = LocalContext.current

    LazyRow {
        item {
            YourStoryItem(onClickNewStory = {
                Toast.makeText(context, "Nueva historia clickeada", Toast.LENGTH_SHORT).show()
            })
        }
        items(stories) {
            StoryItem(story = it, onStoryClick = {
                Toast.makeText(
                    context,
                    "Story clickeada para el usuario: ${it.userName}",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }
}

@Composable
fun YourStoryItem(onClickNewStory: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Box {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Story Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(3.dp, 5.dp, 0.dp, 0.dp)
                    .size(55.dp)
                    .clip(CircleShape)
                    .clickable {
                        onClickNewStory()
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_plus_blue),
                contentDescription = "Add Icon",
                modifier = Modifier
                    .size(15.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        onClickNewStory()
                    }
            )
        }

        Text(
            text = "Your Story",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun StoryItem(story: Story, onStoryClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(61.dp)
                .clip(CircleShape)
                .border(1.dp, Color(0xFFBA4071), CircleShape)
        ) {
            Image(
                painter = painterResource(id = story.id),
                contentDescription = "Story Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(3.dp, 3.dp, 0.dp, 0.dp)
                    .size(55.dp)
                    .clip(CircleShape)
                    .clickable {
                        onStoryClick()
                    }
            )
        }
        Text(
            text = story.userName,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Instagram Logo",
            modifier = Modifier.height(40.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "Add Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = "Favorite Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)

        )
        Icon(
            painter = painterResource(id = R.drawable.ic_chat),
            contentDescription = "Chat Icon",
            modifier = Modifier
                .padding(8.dp, 8.dp, 0.dp, 8.dp)
                .size(24.dp)

        )
    }

}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    PracticaInstagramMVVMTheme {
        Column {
            PostItem(
                Post(
                    1,
                    R.drawable.profile,
                    "user1",
                    "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
                    "user3",
                    "2023-06-01",
                    "Esta es una descripción de la publicación 1"
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryItemPreview() {
    PracticaInstagramMVVMTheme {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(80.dp)
        ) {
            StoryItem(story = Story(R.drawable.profile, "user1"), onStoryClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryPanelPreview() {
    PracticaInstagramMVVMTheme {
        StoryPanel()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    PracticaInstagramMVVMTheme {
        Header()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticaInstagramMVVMTheme {
        HomeScreen()
    }
}