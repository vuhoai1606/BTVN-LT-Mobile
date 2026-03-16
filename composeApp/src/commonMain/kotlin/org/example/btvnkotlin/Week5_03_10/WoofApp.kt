package org.example.btvnkotlin.Week5_03_10

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import btvnkotlin.composeapp.generated.resources.*
import btvnkotlin.composeapp.generated.resources.ic_woof_logo
import org.example.btvnkotlin.Week5_03_10.ui.theme.WoofTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun WoofApp() {
    WoofTheme {
        Scaffold(
            topBar = { WoofTopAppBar() }
        ) { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                // Gọi biến dogs từ file Dog.kt
                items(dogs) { dog ->
                    DogItem(
                        dog = dog,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LOGO
                Image(
                    painter = painterResource(Res.drawable.ic_woof_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                )
                Text(
                    text = "Woof",
                    // Sử dụng font displayLarge (Abril Fatface) mà ta đã định nghĩa
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun DogItem(dog: Dog, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Đã thay Box bằng Image thật
                Image(
                    painter = painterResource(dog.imageResourceId), // Load ảnh từ Compose Resources
                    contentDescription = dog.name,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.small), // Cắt ảnh thành hình tròn
                    contentScale = ContentScale.Crop // Cắt cúp ảnh cho vừa vặn không bị méo
                )

                DogInformation(dog.name, dog.age)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (expanded) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About:", fontWeight = FontWeight.Bold)
                    Text(dog.hobbies)
                }
            }
        }
    }
}

@Composable
fun DogInformation(dogName: String, dogAge: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = dogName,
            // Sử dụng font displayMedium (Montserrat Bold)
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "$dogAge years old",
            // Sử dụng font bodyLarge (Montserrat Regular)
            style = MaterialTheme.typography.bodyLarge
        )
    }
}