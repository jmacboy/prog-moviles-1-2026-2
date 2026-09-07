package com.example.practicainstagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

}

@Composable
fun PostList() {
    val posts = arrayListOf(
        Post(
            R.drawable.profile,
            "user1",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "2023-06-01",
            "Esta es una descripción de la publicación 1"
        ),
        Post(
            R.drawable.profile,
            "user2",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "2023-06-02",
            "Esta es una descripción de la publicación 2"
        ),
        Post(
            R.drawable.profile,
            "user1",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "2023-06-01",
            "Esta es una descripción de la publicación 1"
        ),
        Post(
            R.drawable.profile,
            "user2",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "2023-06-02",
            "Esta es una descripción de la publicación 2"
        ),
        Post(
            R.drawable.profile,
            "user1",
            "https://cdn.pixabay.com/photo/2018/08/18/18/50/sunset-3615276_1280.jpg",
            "user3",
            "2023-06-01",
            "Esta es una descripción de la publicación 1"
        ),
        Post(
            R.drawable.profile,
            "user2",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            "user4",
            "2023-06-02",
            "Esta es una descripción de la publicación 2"
        ),
    )
    LazyColumn {
        items(posts) {
            PostItem(it)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Column {
        PostHeader(post)
        PostImage(post)
        PostActions(post)
        PostReactions(post)
        PostDate(post)
    }
}

@Composable
fun PostDate(post: Post) {
    TODO("Not yet implemented")
}

@Composable
fun PostReactions(post: Post) {
    TODO("Not yet implemented")
}

@Composable
fun PostActions(post: Post) {
    TODO("Not yet implemented")
}

@Composable
fun PostImage(post: Post) {
    TODO("Not yet implemented")
}

@Composable
fun PostHeader(post: Post) {
    TODO("Not yet implemented")
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