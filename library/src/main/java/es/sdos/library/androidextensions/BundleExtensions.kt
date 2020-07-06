package es.sdos.library.androidextensions

import android.os.Bundle

fun Bundle.doIfContains(vararg keys: String, doCall: (bundle: Bundle) -> Unit) {
    if (this.keySet().containsAll(keys.toList())) {
        doCall(this)
    }
}