package com.example.keepall.spannedconverter

import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.HtmlCompat
import androidx.core.text.toHtml
import com.example.keepall.ui.theme.Typography
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class HtmlConverter {

    private data class UnsupportedElement(
        val start: Int,
        val style: SpanStyle,
        val end: Int
    )

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
                    RelativeSizeSpan(1.5f),
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
                    spannable.setSpan(
                        RelativeSizeSpan(1.0f),
                        style.start,
                        style.end,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
            }
        }

        return spannable.toHtml()
    }

    fun toAnnotatedString(html: String): AnnotatedString {
        val spanned = Html.fromHtml(
            html,
            Html.FROM_HTML_MODE_COMPACT
        )
        return buildAnnotatedString {
            append(spanned.toString())
            spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
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

            val parserFactory = XmlPullParserFactory.newInstance()
            parserFactory.isNamespaceAware = true
            val parser = parserFactory.newPullParser()
            var escapedSpans = html.replace("<span ", "&lt;span ", true)
            escapedSpans = escapedSpans.replace("</span>", "&lt;/span&gt;", true)
            val cooo = HtmlCompat.fromHtml(escapedSpans, HtmlCompat.FROM_HTML_MODE_COMPACT)
            val sb = SpannableStringBuilder(cooo)
            parser.setInput(sb.toString().reader())
            val unsupportedElements: MutableList<UnsupportedElement> = mutableListOf()
            var currentIndex = 0
            var eventType = XmlPullParser.START_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.getAttributeValue(0) == "font-size:1.50em;")
                            unsupportedElements.add(
                                UnsupportedElement(
                                    currentIndex,
                                    SpanStyle(fontSize = Typography.headlineLarge.fontSize),
                                    0
                                )
                            )
                        else if (parser.getAttributeValue(0) == "font-size:1.40em;")
                            unsupportedElements.add(
                                UnsupportedElement(
                                    currentIndex,
                                    SpanStyle(fontSize = Typography.headlineMedium.fontSize),
                                    0
                                )
                            )
                        else
                            unsupportedElements.add(
                                UnsupportedElement(
                                    currentIndex,
                                    SpanStyle(fontSize = TextUnit.Unspecified),
                                    0
                                )
                            )
                    }

                    XmlPullParser.END_TAG -> {
                        for (i in unsupportedElements.size - 1 downTo 0) {
                            if (unsupportedElements[i].end == 0)
                                unsupportedElements[i] =
                                    unsupportedElements[i].copy(end = currentIndex)
                        }
                    }

                    XmlPullParser.TEXT -> {
                        currentIndex += parser.text.length
                    }
                }
                eventType = parser.next()
            }
            for (item in unsupportedElements) {
                addStyle(item.style, item.start, item.end)
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

        val parserFactory = XmlPullParserFactory.newInstance()
        parserFactory.isNamespaceAware = true
        val parser = parserFactory.newPullParser()
        var escapedSpans = html.replace("<span ", "&lt;span ", true)
        escapedSpans = escapedSpans.replace("</span>", "&lt;/span&gt;", true)
        val cooo = HtmlCompat.fromHtml(escapedSpans, HtmlCompat.FROM_HTML_MODE_COMPACT)
        val sb = SpannableStringBuilder(cooo)
        parser.setInput(sb.toString().reader())
        val unsupportedElements: MutableList<UnsupportedElement> = mutableListOf()
        var currentIndex = 0
        var eventType = XmlPullParser.START_DOCUMENT
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.getAttributeValue(0) == "font-size:1.50em;")
                        unsupportedElements.add(
                            UnsupportedElement(
                                currentIndex,
                                SpanStyle(fontSize = Typography.headlineLarge.fontSize),
                                0
                            )
                        )
                    else if (parser.getAttributeValue(0) == "font-size:1.40em;")
                        unsupportedElements.add(
                            UnsupportedElement(
                                currentIndex,
                                SpanStyle(fontSize = Typography.headlineMedium.fontSize),
                                0
                            )
                        )
                    else
                        unsupportedElements.add(
                            UnsupportedElement(
                                currentIndex,
                                SpanStyle(fontSize = TextUnit.Unspecified),
                                0
                            )
                        )
                }

                XmlPullParser.END_TAG -> {
                    for (i in unsupportedElements.size - 1 downTo 0) {
                        if (unsupportedElements[i].end == 0)
                            unsupportedElements[i] = unsupportedElements[i].copy(end = currentIndex)
                    }
                }

                XmlPullParser.TEXT -> {
                    currentIndex += parser.text.length
                }
            }
            eventType = parser.next()
        }
        for (item in unsupportedElements) {
            styleList.add(
                AnnotatedString.Range<SpanStyle>(
                    item.style, item.start, item.end
                )
            )
        }

        return styleList
    }
}