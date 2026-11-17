package com.bym.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF100D19)
private val Surface = Color(0xFF211A30)
private val Purple = Color(0xFF9B72F2)
private val Muted = Color(0xFFB9AECF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BYMApp() }
    }
}

data class FeedPost(val author: String, val handle: String, val text: String, val likes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BYMApp() {
    var selected by remember { mutableStateOf("Home") }
    var draft by remember { mutableStateOf("") }
    var posts by remember {
        mutableStateOf(
            listOf(
                FeedPost("Alex Morgan", "@alexm", "Chasing dreams, one day at a time. ✨", 128),
                FeedPost("Sarah Lee", "@sarah", "Finding beauty in the little things.", 86),
                FeedPost("David King", "@davidk", "Create your own path. Never stop.", 54)
            )
        )
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            background = Bg,
            surface = Surface,
            onPrimary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Bg,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("BYM", fontWeight = FontWeight.ExtraBold, fontSize = 25.sp, color = Purple)
                            Text("BE YOUR MOMENT", fontSize = 9.sp, color = Muted, letterSpacing = 2.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Bg)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Surface) {
                    val tabs = listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Explore", Icons.Default.Explore, "Explore"),
                        Triple("Create", Icons.Default.Add, "Create"),
                        Triple("Likes", Icons.Default.FavoriteBorder, "Likes"),
                        Triple("Profile", Icons.Default.Person, "Profile")
                    )
                    tabs.forEach { (label, icon, route) ->
                        NavigationBarItem(
                            selected = selected == route,
                            onClick = { selected = route },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Purple,
                                selectedTextColor = Purple,
                                indicatorColor = Color(0xFF392A56),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (selected) {
                "Home" -> LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text("Your world. Your moments.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Welcome to your BYM community.", color = Muted, fontSize = 13.sp)
                    }
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Share a moment", fontWeight = FontWeight.SemiBold)
                                OutlinedTextField(
                                    value = draft,
                                    onValueChange = { draft = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("What's happening?", color = Muted) },
                                    minLines = 2,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Purple,
                                        unfocusedBorderColor = Color(0xFF514568),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                                Button(
                                    onClick = {
                                        if (draft.isNotBlank()) {
                                            posts = listOf(FeedPost("You", "@you", draft.trim(), 0)) + posts
                                            draft = ""
                                        }
                                    },
                                    enabled = draft.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Post")
                                }
                            }
                        }
                    }
                    item { Text("Community feed", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    items(posts) { post -> PostCard(post) }
                }
                "Explore" -> SimplePage("Explore BYM", "Discover creators, topics and communities.")
                "Create" -> SimplePage("Create a moment", "Write a post from the Home tab. Photo and video uploads are coming next.")
                "Likes" -> SimplePage("Your activity", "Posts you like will appear here when accounts and the backend are connected.")
                else -> SimplePage("Your profile", "Public profile · Username, bio, followers and your posts will appear here.")
            }
        }
    }
}

@Composable
private fun PostCard(post: FeedPost) {
    var liked by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp).background(Color(0xFF493568), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(post.author.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(post.author, fontWeight = FontWeight.SemiBold)
                    Text(post.handle, color = Muted, fontSize = 12.sp)
                }
            }
            Text(post.text, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { liked = !liked }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Like", tint = if (liked) Purple else Muted)
                    Spacer(Modifier.width(5.dp))
                    Text(if (liked) "Liked" else "Like", color = if (liked) Purple else Muted)
                }
                Text("${post.likes + if (liked) 1 else 0} likes", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text("Comment", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SimplePage(title: String, subtitle: String) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Purple)
        Spacer(Modifier.height(10.dp))
        Text(subtitle, color = Muted)
    }
}
