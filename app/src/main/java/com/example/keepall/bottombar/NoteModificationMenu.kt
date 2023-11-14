package com.example.keepall.bottombar


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.keepall.R

@Composable
fun NoteModificationMenu(
    addIconAction: () -> Unit,
    paletteIconAction: () -> Unit,
    textIconAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Add attachments",
            modifier = Modifier
                .clickable {
                    addIconAction()
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.baseline_palette_24),
            contentDescription = "Add your painting",
            modifier = Modifier
                .clickable {
                    paletteIconAction()
                }
        )
        Icon(painter = painterResource(id = R.drawable.text_format_24px),
            contentDescription = "Font options",
            modifier = Modifier
                .clickable {
                    textIconAction()
                }

        )
    }
}
