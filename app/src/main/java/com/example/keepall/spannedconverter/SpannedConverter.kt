package com.example.keepall.spannedconverter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat
import androidx.core.text.toHtml
import com.example.keepall.ui.theme.Typography

class HtmlConverter {
    private val SIZE_PATTERN = Regex("<span style=\"font-size:\\d\\.\\d\\dem;\">.*</span>")
    fun convertToHtml(annotatedString: AnnotatedString): String {
        val spannable = SpannableStringBuilder(annotatedString.text)
        annotatedString.spanStyles.forEach { style ->
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
            when (style.item.fontSize) {
                Typography.headlineLarge.fontSize -> spannable.setSpan(
                    AbsoluteSizeSpan(14),
                    style.start,
                    style.end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                Typography.headlineMedium.fontSize -> spannable.setSpan(
                    RelativeSizeSpan(1.4f),
                    style.start,
                    style.end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

                else -> {
                    //do absolutely nothing
                }
            }
        }

        return spannable.toHtml()
    }

    fun toAnnotatedString(spanned: Spanned): AnnotatedString =
        buildAnnotatedString {
            append(spanned.toString())
            spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
                println(span::class.java.name)
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                when (span) {
                    is StyleSpan -> when (span.style) {
                        Typeface.BOLD -> addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start,
                            end
                        )

                        Typeface.ITALIC -> addStyle(
                            SpanStyle(fontStyle = FontStyle.Italic),
                            start,
                            end
                        )

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

    fun getStyles(html: String): List<AnnotatedString.Range<SpanStyle>> {
        val realHtml = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        val styleList = mutableListOf<AnnotatedString.Range<SpanStyle>>()
        realHtml.getSpans(0, realHtml.length, Any::class.java).forEach { span ->
            val start = realHtml.getSpanStart(span)
            val end = realHtml.getSpanEnd(span)
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
        //handle unsupported tags
/*                var escapedSpans = html.replace("<span ", "&lt;span ", true)
                escapedSpans = escapedSpans.replace("</span>", "&lt;/span&gt;", true)
                val cooo = HtmlCompat.fromHtml(escapedSpans, HtmlCompat.FROM_HTML_MODE_COMPACT)
                val sb = SpannableStringBuilder(cooo)

                val m: Matcher = SIZE_PATTERN.toPattern().matcher(sb)
                do {
                    if(m.find()) {
                        println("${m.start()} ${m.group()}  ${m.end()} ")
                    }
                } while (!m.hitEnd())*/
        return styleList
    }
}