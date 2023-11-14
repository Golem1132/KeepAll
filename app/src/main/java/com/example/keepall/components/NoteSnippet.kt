package com.example.keepall.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keepall.data.Note
import com.example.keepall.spannedconverter.HtmlConverter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteSnippet(
    item: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isMarked: Boolean
) {
    val htmlConverter = HtmlConverter()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                if (!isMarked)
                    1.dp
                else
                    3.dp,
                if (!isMarked)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.tertiaryContainer,
                RoundedCornerShape(5)
            )
            .clip(RoundedCornerShape(5))
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            ),
        shape = RoundedCornerShape(5),
        color = Color(item.color)
    ) {
        Column(modifier = Modifier.heightIn(100.dp, 250.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = item.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                overflow = TextOverflow.Ellipsis,
                text = htmlConverter.toAnnotatedString(item.textContent)
            )

        }
    }
}
