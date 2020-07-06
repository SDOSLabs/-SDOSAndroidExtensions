package es.sdos.library.androidextensions

import android.view.View

fun List<View?>.notNull() = none { it == null }