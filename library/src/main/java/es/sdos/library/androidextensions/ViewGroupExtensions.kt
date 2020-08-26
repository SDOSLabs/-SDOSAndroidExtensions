package es.sdos.library.androidextensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout

//region CoordinatorLayout Extensions
fun CoordinatorLayout.enableAppBarBehaviour(enable: Boolean) {
    for (childView in children) {
        if (childView is FragmentContainerView) {
            val layoutParams = childView.layoutParams as CoordinatorLayout.LayoutParams
            if (enable) {
                layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
            } else {
                layoutParams.behavior = null
            }
            childView.requestLayout()
        }
    }
}
//endregion

//region RecyclerView Extensions
fun RecyclerView.addDefaultItemDecoration(
    @DrawableRes resourceDrawableId: Int,
    orientation: Int = DividerItemDecoration.VERTICAL
) = addItemDecoration(
    DividerItemDecoration(
        context,
        orientation
    ).apply {
        ContextCompat.getDrawable(context, resourceDrawableId)?.let {
            setDrawable(it)
        }
    }
)

fun RecyclerView.ViewHolder.getString(@StringRes stringRes: Int): String {
    return itemView.context.getString(stringRes)
}

fun RecyclerView.ViewHolder.getContext(): Context {
    return itemView.context
}
//endregion

//region Toolbar Extensions
fun Toolbar.setNavigationIcon(@DrawableRes drawableId: Int) {
    navigationIcon = AppCompatResources.getDrawable(context, drawableId)
}
//endregion

//region ViewGroup Extensions
fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ViewGroup.children() = object : Iterable<View> {
    override fun iterator() = object : Iterator<View> {
        var index = 0
        override fun hasNext() = index < childCount
        override fun next() = getChildAt(index++)
    }
}
//endregion



