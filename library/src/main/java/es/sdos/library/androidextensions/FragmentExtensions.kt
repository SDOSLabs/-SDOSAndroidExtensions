package es.sdos.library.androidextensions

import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.popBack() = findNavController().navigateUp()

/**
 * Returns the view with active focus or null if not exist.
 */
fun Fragment.getFocusedView(): View? = activity?.currentFocus ?: view?.rootView

fun Fragment.showErrorDialog(
    acceptMessage: String,
    errorMessage: String,
    @ColorRes acceptColorResId: Int
) = context?.let { con ->
    val dialog = Builder(con)
        .setCancelable(true)
        .setMessage(errorMessage)
        .setTitle(null)
        .setPositiveButton(acceptMessage, null)
        .create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(con, acceptColorResId))
    }
    dialog.show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, message, duration).show()
}

fun Fragment.showSnackbar(snackbarText: String?, timeLength: Int = Snackbar.LENGTH_SHORT) {
    activity?.let {
        if (snackbarText != null) {
            Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength).show()
        }
    }
}

fun Fragment.getSafeString(@StringRes resId: Int) = context?.getString(resId).orEmpty()

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        func()
        commit()
    }
}