package es.sdos.library.androidextensions

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.getSafeString(@StringRes resId: Int) = resources?.getString(resId).orEmpty()