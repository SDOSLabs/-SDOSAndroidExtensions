package es.sdos.library.androidextensions

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.getSafeString(@StringRes resId: Int) = resources?.getString(resId).orEmpty()

/**
 * Return the instance if activity is active or return null in otherwise
 */
fun Activity.isActive() =
    if (this.canUse()) {
        this
    } else {
        null
    }

/**
 * Return true if activity is not null and not finishing
 */
fun Activity.canUse() = this.isFinishing.not()