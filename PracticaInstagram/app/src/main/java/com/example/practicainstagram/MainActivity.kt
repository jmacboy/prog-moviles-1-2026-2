package com.example.practicainstagram

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.practicainstagram.ui.theme.PracticaInstagramTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaInstagramTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Header()
        StoryPanel()
        PostList()
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
fun ColumnScope.PostList() {
    val posts = remember {
        mutableStateListOf(
            Post(
                R.drawable.profile,
                "juanito",
                "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
                "user3",
                "1 de enero",
                "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ),
            Post(
                R.drawable.profile,
                "pepito",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
                "user4",
                "12 de noviembre",
                "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ),
            Post(
                R.drawable.profile,
                "pedrito",
                "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
                "user3",
                "1 de enero",
                "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ),
            Post(
                R.drawable.profile,
                "user2",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
                "user4",
                "2 de febrero",
                "lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ),
            Post(
                R.drawable.profile,
                "user1",
                "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
                "user3",
                "1 de enero",
                "Esta es una descripción de la publicación 1"
            ),
            Post(
                R.drawable.profile,
                "user2",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
                "user4",
                "2 de marzo",
                "Esta es una descripción de la publicación 2"
            ),
        )
    }
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(posts) {
            PostItem(it, onLikeClick = {
                val index = posts.indexOf(it)
                it.isLiked = !it.isLiked
                if (it.isLiked) {
                    Toast.makeText(context, "Post likeado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Post deslikeado", Toast.LENGTH_SHORT).show()
                }
                posts[index] = it
            })
        }
    }
}

@Composable
fun PostItem(post: Post, onLikeClick: () -> Unit) {
    Column {
        PostHeader(post)
        PostImage(post)
        PostActions(post, onLikeClick)
        PostReactions(post)
        PostDescription(post)
        PostDate(post)
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
fun PostActions(post: Post, onLikeClick: () -> Unit) {
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
                    onLikeClick()
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_message),
            contentDescription = "Comment Icon",
            modifier = Modifier
                .padding(6.dp)
                .size(30.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "Share Icon",
            modifier = Modifier
                .padding(8.dp, 10.dp, 8.dp, 8.dp)
                .size(22.dp)
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
fun StoryPanel() {
    val stories = arrayListOf(
        Story(R.drawable.profile, "user1"),
        Story(R.drawable.profile, "user2"),
        Story(R.drawable.profile, "user3"),
        Story(R.drawable.profile, "user4"),
        Story(R.drawable.profile, "user5"),
        Story(R.drawable.profile, "user6"),
    )
    LazyRow {
        item {
            YourStoryItem()
        }
        items(stories) {
            StoryItem(story = it)
        }
    }
}

@Composable
fun YourStoryItem() {
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
            )
            Image(
                painter = painterResource(id = R.drawable.ic_plus_blue),
                contentDescription = "Add Icon",
                modifier = Modifier
                    .size(15.dp)
                    .align(Alignment.BottomEnd)
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
fun StoryItem(story: Story) {
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
    PracticaInstagramTheme {
        Column {
            PostItem(
                Post(
                    R.drawable.profile,
                    "user1",
                    "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
                    "user3",
                    "2023-06-01",
                    "Esta es una descripción de la publicación 1"

                ), onLikeClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryItemPreview() {
    PracticaInstagramTheme {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(80.dp)
        ) {
            StoryItem(story = Story(R.drawable.profile, "user1"))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryPanelPreview() {
    PracticaInstagramTheme {
        StoryPanel()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    PracticaInstagramTheme {
        Header()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PracticaInstagramTheme {
        HomeScreen()
    }
}