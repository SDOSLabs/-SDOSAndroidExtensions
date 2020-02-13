package es.sdos.library.androidextensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import androidx.annotation.ColorInt

fun String.isEmail(): Boolean = (isNotBlank() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun String.isURL(): Boolean = (isNotBlank() &&
        Patterns.WEB_URL.matcher(this).matches())

fun SpannableStringBuilder.addClickableSpan(span: ClickableSpan, textToBeMadeClickable: String,
                                            @ColorInt colorInt: Int) : SpannableStringBuilder {
    val start = indexOf(textToBeMadeClickable)
    if (start != -1) {
        val end = start + textToBeMadeClickable.length
        setSpan(span, start, end, 0)
        setSpan(ForegroundColorSpan(colorInt), start, end, 0)
    }
    return this
}

fun SpannableStringBuilder.setBoldStyle(textToBeMadeClickable: String): SpannableStringBuilder {
    val start = indexOf(textToBeMadeClickable)
    if (start != -1) {
        val end = start + textToBeMadeClickable.length
        val bold = StyleSpan(Typeface.BOLD)
        setSpan(bold, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }
    return this
}