package com.example.keepall.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun ElevatedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onTextChange: (String) -> Unit,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    error: Boolean = false,
    shape: Shape = RoundedCornerShape(20),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {}
) {
    BasicTextField(
        modifier = modifier
            .padding(8.dp)
            .defaultMinSize(280.dp, 56.dp),
        textStyle = LocalTextStyle.current,
        value = value,
        onValueChange = onTextChange,
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = {
            //Container
            Box(
                modifier = Modifier
                    .shadow(
                        1.dp, shape,
                        false
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            1.dp
                        ), shape = shape
                    )
                    .clip(shape)
                    .then(
                        if (error)
                            Modifier.border(1.dp, Color.Red, shape)
                        else Modifier
                    )
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Layout(content = {
                    leadingIcon()
                    Box(modifier = Modifier.graphicsLayer {
                        alpha = if (value.isEmpty())
                            0.5f
                        else
                            1f
                    }) {
                        if (value.isEmpty())
                            placeholder()
                        it()
                    }
                    trailingIcon()
                }) { things, constraints ->
                    val height = (56 * density).roundToInt()
                    val paddingH = (16 * density).roundToInt()
                    val leadingIconPlaceable = things[0].measure(constraints)
                    val trailingIconPlaceable = things[2].measure(constraints)
                    val innerTextFieldPlaceable = things[1].measure(
                        constraints.copy(
                            minWidth = constraints.minWidth,
                            maxWidth = constraints.maxWidth - leadingIconPlaceable.width - trailingIconPlaceable.width - (paddingH * 4),
                            minHeight = constraints.minHeight,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(width = constraints.maxWidth, height = height) {
                        leadingIconPlaceable.place(
                            paddingH,
                            (height / 2) - (leadingIconPlaceable.height / 2)
                        )
                        innerTextFieldPlaceable.place(
                            leadingIconPlaceable.width + (paddingH * 2),
                            (height / 2) - innerTextFieldPlaceable.height / 2
                        )
                        trailingIconPlaceable.place(
                            constraints.maxWidth - trailingIconPlaceable.width - paddingH,
                            (height / 2) - (trailingIconPlaceable.height / 2)
                        )
                    }
                }
            }
        })

}

@Composable
fun ElevatedTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    error: Boolean = false,
    shape: Shape = RoundedCornerShape(20),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {}
) {
    BasicTextField(
        modifier = modifier
            .padding(8.dp)
            .defaultMinSize(280.dp, 56.dp),
        textStyle = LocalTextStyle.current,
        value = value,
        onValueChange = onTextChange,
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = {
            //Container
            Box(
                modifier = Modifier
                    .shadow(
                        1.dp, shape,
                        false
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            1.dp
                        ), shape = shape
                    )
                    .clip(shape)
                    .then(
                        if (error)
                            Modifier.border(1.dp, Color.Red, shape)
                        else Modifier
                    )
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Layout(content = {
                    leadingIcon()
                    Box(modifier = Modifier.graphicsLayer {
                        alpha = if (value.text.isEmpty())
                            0.5f
                        else
                            1f
                    }) {
                        if (value.text.isEmpty())
                            placeholder()
                        it()
                    }
                    trailingIcon()
                }) { things, constraints ->
                    val height = (56 * density).roundToInt()
                    val paddingH = (16 * density).roundToInt()
                    val leadingIconPlaceable = things[0].measure(constraints)
                    val trailingIconPlaceable = things[2].measure(constraints)
                    val innerTextFieldPlaceable = things[1].measure(
                        constraints.copy(
                            minWidth = constraints.minWidth,
                            maxWidth = constraints.maxWidth -
                                    leadingIconPlaceable.width -
                                    (if (value.text.length > 0)
                                        trailingIconPlaceable.width
                                    else 0) - (paddingH * 4),
                            minHeight = constraints.minHeight,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(width = constraints.maxWidth, height = height) {
                        leadingIconPlaceable.place(
                            paddingH,
                            (height / 2) - (leadingIconPlaceable.height / 2)
                        )
                        innerTextFieldPlaceable.place(
                            leadingIconPlaceable.width + (paddingH * 2),
                            (height / 2) - innerTextFieldPlaceable.height / 2
                        )
                        if (value.text.length > 0)
                            trailingIconPlaceable.place(
                                constraints.maxWidth - trailingIconPlaceable.width - paddingH,
                                (height / 2) - (trailingIconPlaceable.height / 2)
                            )
                    }
                }
            }
        })

}