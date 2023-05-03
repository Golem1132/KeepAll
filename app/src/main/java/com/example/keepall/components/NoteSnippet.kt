package com.example.keepall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.keepall.R
import com.example.keepall.data.Note
import com.example.keepall.ui.theme.Yellow20
import com.example.keepall.utils.fromStringToList

@Composable
fun NoteSnippet(
    modifier: Modifier,
    note: Note
) {
    val canvasCount = if (!note.canvas.isNullOrBlank()) 1 else 0
    val photosCount = fromStringToList(note.photos).size
//        .background(color = Yellow20)
//        .heightIn(100.dp, 200.dp)


    Scaffold(modifier = modifier,
        containerColor = Yellow20,
        contentWindowInsets = WindowInsets(10.dp,10.dp,10.dp,0.dp),
        bottomBar = {
            NoteBottomBar(canvasCount, photosCount, false)
        }) {
        Text(
            modifier = Modifier
                .padding(it),
            softWrap = true,
            text = note.textContent,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun NoteBottomBar(canvasCount: Int, photosCount: Int, isInCalendar: Boolean) {
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter =
                if (isInCalendar)
                    painterResource(id = R.drawable.event_available_24px)
                else
                    painterResource(id = R.drawable.event_busy_24px),
                contentDescription = "canvas count"
            )
        }


    }
}
