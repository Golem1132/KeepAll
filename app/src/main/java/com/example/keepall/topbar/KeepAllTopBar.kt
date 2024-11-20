package com.example.keepall.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keepall.components.ElevatedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAllTTopBar(
    state: KeepAllTopBarState,
    onKeyboardAction: (String) -> Unit,
    onActionClick: () -> Unit
) {
    var textFieldValue by remember(state.search.value) {
        mutableStateOf(
            TextFieldValue(
                text = state.search.value,
                selection = TextRange(state.search.value.length)
            )
        )
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val localFocusManager = LocalFocusManager.current
    TopAppBar(title = {
        ElevatedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {

                },
            value = textFieldValue, onTextChange = {
                state.search.value = it.text
                textFieldValue = it
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        state.search.value = ""
                    },
                    imageVector = Icons.Default.Clear, contentDescription = ""
                )
            },
            placeholder = {
                Text(
                    text = "Looking for something, but it is very long, longer than you think ?",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            keyboardActions = KeyboardActions(onSearch = {
                localFocusManager.clearFocus()
                onKeyboardAction(state.search.value)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
    },
        actions = {
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clickable {
                        onActionClick()
                    },
                imageVector = Icons.Default.Person, contentDescription = ""
            )
        })
}

@Preview
@Composable
fun PreviewKeepAllTopBar() {
    val state = rememberKeepAllTopBarState()
    KeepAllTTopBar(state, {_ ->}, {})
}

