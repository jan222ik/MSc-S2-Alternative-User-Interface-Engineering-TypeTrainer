package ui.util.span_parse

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

object SpanStringParser  {
    fun String.parseForSpans(spanStyle: SpanStyle) = parseForSpans(provideSpanStyleFor = { _, _ -> spanStyle })


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
}
