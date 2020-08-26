package es.sdos.library.androidextensions

import android.animation.ObjectAnimator
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

private const val VIEW_ANIMATION_REPEAT = 950L
private const val DURATION_SHORT = 300L
private const val DURATION_MIN = 150L
private const val NO_COLOR = Int.MAX_VALUE
private const val ALPHA_PROPERTY = "alpha"
private const val FULL_ALPHA = 1F
private const val NO_ALPHA = 0F

//region View Extensions
fun <T : View> View.bindView(@IdRes res: Int): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE){ findViewById<T>(res) }

fun <T : View> View.bindViews(@IdRes resources: Array<Int>): List<Lazy<T>> =
    resources.map { bindView<T>(it) }.toList()

fun View.getStyleTypeArray(
    attrs: AttributeSet?,
    @StyleableRes styleId: IntArray
): TypedArray = context.obtainStyledAttributes(
    attrs,
    styleId,
    0,
    0
)

fun View.getColor(colorRes: Int) = ResourcesCompat.getColor(resources, colorRes, null)

fun View.getColorFromBackground() = when (background) {
    is ColorDrawable -> (background as ColorDrawable).color
    is RippleDrawable -> background.state[0]
    else -> NO_COLOR
}

fun View.hideKeyboard() {
    context?.let {
        val imm = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}

/**
 * Put visibility to Gone.
 * @param condition -> if this parameter is passed, it is checked whether or not it is met to apply the visibility change. Default value is true.
 * If condition is false, the view put as [View.VISIBLE]
 */
fun View.hide(condition: Boolean = true, doWhenGone: () -> Unit = {}) {
    val isVisible = this.isVisible
    this.visibility = if (isVisible &&
        condition) {
            doWhenGone()
            GONE
        } else {
            VISIBLE
        }

}

/**
 * Put visibility to Visible.
 * @param condition -> if this parameter is passed, it is checked whether or not it is met to apply the visibility change. Default value is true.
 * If condition is false, the view put as [View.GONE]
 */
fun View.show(condition: Boolean = true, doWhenVisible: () -> Unit = {}) {
    val isVisible = this.isVisible
    this.visibility = if (isVisible.not() &&
        condition) {
            doWhenVisible()
            VISIBLE
        } else {
            GONE
        }
}

/**
 * Put visibility to Invisible.
 * @param condition -> if this parameter is passed, it is checked whether or not it is met to apply the visibility change. Default value is true.
 * If condition is false, the view put as [View.VISIBLE]
 */
fun View.invisible(condition: Boolean = true) {
    val isInvisible = this.isInvisible
    this.visibility = if (isInvisible.not() &&
                condition) {
            INVISIBLE
        } else {
            VISIBLE
        }
}

fun View.setMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val layoutParams = this.layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.setMargins(left, top, right, bottom)
        this.layoutParams = layoutParams
    }
}

fun View.showKeyboard() {
    val inputMethodManager = this.context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.animateAlpha(visible: Boolean, duration: Long = DURATION_SHORT) {
    val alpha = if (visible) {
        FULL_ALPHA
    } else {
        NO_ALPHA
    }
    val animation = ObjectAnimator.ofFloat(this, ALPHA_PROPERTY, alpha)
    animation.apply {
        this.duration = duration
        start()
    }
}

fun View.getLayoutInflater(): LayoutInflater = LayoutInflater.from(this.context)

fun View.getActivity(): FragmentActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is FragmentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
//endregion

//region ImageView Extensions
fun ImageView.animateVector(imageRes: Int, duration: Long = VIEW_ANIMATION_REPEAT) {
    val animateVectorDrawable = AnimatedVectorDrawableCompat.create(context, imageRes)
    animateVectorDrawable?.let {
        it.registerAnimationCallback(object :
            Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                Handler().postDelayed({ it.start() }, duration)
            }
        })
        setImageDrawable(it)
        it.start()
    }
}

fun ImageView.setImageResourceWithAnimation(@DrawableRes resId: Int) {
    applyFadeInAfterFadeOut {
        setImageResource(resId)
    }
}
//endregion

//region TextView Extensions
fun TextView.isEmpty() = TextUtils.isEmpty(text)

fun TextView.setTextOrHideIfEmpty(text: String?) {
    this.text = text
    visibility = if (text.isNullOrEmpty()) {
        GONE
    } else {
        VISIBLE
    }
}

fun TextView.setTextResourceWithAnimation(@StringRes resId: Int) {
    applyFadeInAfterFadeOut {
        text = context.getText(resId)
    }
}

fun TextView.setTextWithAnimation(newText: String) {
    applyFadeInAfterFadeOut {
        text = newText
    }
}

fun View.applyFadeInAfterFadeOut(doFunctionBeforeStartFadeInAnimation: () -> Unit) {
    val fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    fadeIn.duration = DURATION_MIN
    val fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    fadeOut.apply {
        duration = DURATION_MIN
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                //- no-op
            }

            override fun onAnimationEnd(p0: Animation?) {
                doFunctionBeforeStartFadeInAnimation()
                startAnimation(fadeIn)
            }

            override fun onAnimationStart(p0: Animation?) {
                //- no-op
            }

        })
    }
    startAnimation(fadeOut)
}

/**
 * Apply scale up animation.
 * @param toScale -> Scale percent quantity
 */
fun TextView.scaleUp(toScale: Float = 0.1f) {
    val scaleUp = ScaleAnimation(1f, 1f + toScale, 1f, 1f + toScale,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f)
    scaleUp.apply {
        reset()
        fillAfter = true
        duration = DURATION_SHORT
    }
    this.clearAnimation()
    this.startAnimation(scaleUp)
}

/**
 * Apply scale down animation.
 * @param toScale -> Scale percent quantity
 */
fun TextView.scaleDown(fromScale: Float = 0.1f) {
    val scaleDown = ScaleAnimation(1f + fromScale, 1f, 1f + fromScale, 1f,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f)
    scaleDown.apply {
        reset()
        fillAfter = true
        duration = DURATION_SHORT
    }
    this.clearAnimation()
    this.startAnimation(scaleDown)
}
//endregion

//region EditText Extensions
fun EditText.isBlank() = this.text.isBlank()
//endregion
