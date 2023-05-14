package com.example.keepall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.keepall.R
import com.example.keepall.model.NoteListItem
import com.example.keepall.ui.theme.Yellow20
import com.example.keepall.utils.fromStringToList

@Composable
fun NoteSnippet(
    modifier: Modifier,
    item: NoteListItem
) {
    val canvasCount = if (!item.note.canvas.isNullOrBlank()) 1 else 0
    val photosCount = fromStringToList(item.note.photos).size


    Scaffold(modifier = modifier
        .then(
            Modifier.border(
                width = 3.dp,
                color = if (item.checked)
                    Color.Blue
                else Color.Transparent
            )
        ),
        containerColor = Yellow20,
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 0.dp),
        bottomBar = {
            NoteBottomBar(canvasCount, photosCount)
        }) {
        Text(
            modifier = Modifier
                .padding(it),
            softWrap = true,
            text = item.note.textContent,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun NoteBottomBar(canvasCount: Int, photosCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.palette_24px),
                contentDescription = "canvas count"
            )
            Text(text = canvasCount.toString())
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_24px),
                contentDescription = "photos count"
            )
            Text(text = photosCount.toString())
        }
    }
}
