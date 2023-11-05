package com.example.keepall.spannedconverter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.toHtml

//SpannableStringBuilder(HtmlCompat.fromHtml("<p dir="ltr"><b>xd naprawd</b>e</p>", HtmlCompat.FROM_HTML_MODE_COMPACT)).toAnnotatedString()

fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ), start, end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start,
                end
            )
        }
    }

}

fun Spanned.extractStyle(): List<AnnotatedString.Range<SpanStyle>> {
    val styleList = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    getSpans(0, this.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> styleList.add(
                    AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                )

                Typeface.ITALIC -> styleList.add(
                    AnnotatedString.Range(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                )

                Typeface.BOLD_ITALIC -> styleList.add(
                    AnnotatedString.Range(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ), start, end
                    )
                )
            }

            is UnderlineSpan -> styleList.add(
                AnnotatedString.Range(
                    SpanStyle(textDecoration = TextDecoration.Underline),
                    start,
                    end
                )
            )
        }
    }
    return styleList
}

fun AnnotatedString.toHtml(): String {
    val spannable = SpannableStringBuilder(this.text)
    this.spanStyles.forEach { style ->
        if (style.item.fontWeight == FontWeight.Bold)
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                style.start,
                style.end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        if (style.item.textDecoration == TextDecoration.Underline)
            spannable.setSpan(
                UnderlineSpan(),
                style.start,
                style.end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        if (style.item.fontStyle == FontStyle.Italic)
            spannable.setSpan(
                StyleSpan(Typeface.ITALIC),
                style.start,
                style.end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
    }

    return spannable.toHtml()
}