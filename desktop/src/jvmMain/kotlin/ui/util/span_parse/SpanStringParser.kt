package ui.util.span_parse

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString


/**
 *  Parses a [String] for span elements and applies a given [SpanStyle] for each of it.
 *  The result is an [AnnotatedString].
 *
 *  @receiver [String] to be parsed for spans.
 *  @param spanStyle [SpanStyle] to be applied to all spans
 *  @return [AnnotatedString] with styles applied to.
 */
fun String.parseForSpans(spanStyle: SpanStyle) = parseForSpans { _, _ -> spanStyle }


/**
 *  Parses a [String] for span elements and applies a given [SpanStyle] for each of it.
 *  The result is an [AnnotatedString].
 *
 *  @receiver [String] to be parsed for spans.
 *  @param provideSpanStyleFor Provider function to produce a [SpanStyle] from a given index and content of the span.
 *  @return [AnnotatedString] with styles applied to.
 */
fun String.parseForSpans(provideSpanStyleFor: (idx: Int, content: String) -> SpanStyle): AnnotatedString {
    return buildAnnotatedString {
        val spanRegex = "<span>(.*?)<\\\\span>".toRegex()
        append(this@parseForSpans.replace("(<span>)|(<\\\\span>)".toRegex(), ""))
        var idxShift = 0
        spanRegex.findAll(this@parseForSpans)
            .forEachIndexed { idx, it ->
                val start = it.range.first.minus(idxShift)
                idxShift += 13
                val end = it.range.last.minus(idxShift).inc()
                addStyle(provideSpanStyleFor(idx, it.groupValues[1]), start, end)
            }
    }
}
