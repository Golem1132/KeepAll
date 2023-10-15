package com.example.keepall.bottombar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.keepall.R

@Composable
fun TextModificationMenu(
    h1: () -> Unit,
    h2: () -> Unit,
    normalText: () -> Unit,
    bold: () -> Unit,
    italic: () -> Unit,
    underline: () -> Unit,
    clear: () -> Unit,
    exit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.clickable { h1() },
            painter = painterResource(id = R.drawable.format_h1_24px), contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { h2() },
            painter = painterResource(id = R.drawable.format_h2_24px),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { normalText() },
            painter = painterResource(id = R.drawable.format_normal_24px),
            contentDescription = ""
        )
        VerticalDivider()
        Icon(
            modifier = Modifier.clickable { bold() },
            painter = painterResource(id = R.drawable.format_bold_24px),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { italic() },
            painter = painterResource(id = R.drawable.format_italic_24px),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { underline() },
            painter = painterResource(id = R.drawable.format_underlined_24px),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { clear() },
            painter = painterResource(id = R.drawable.format_clear_24px),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier.clickable { exit() },
            imageVector = Icons.Default.Close,
            contentDescription = ""
        )

    }

}