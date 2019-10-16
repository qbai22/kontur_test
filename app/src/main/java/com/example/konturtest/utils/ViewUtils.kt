package xunison.com.smarthomeapp.utils

import android.content.Context
import android.view.View

/**
 * Created by Vladimir Kraev
 */


fun getStatusBarHeight(context: Context): Int {

    var height = 0
    val resources = context.resources
    val idStatusBarHeight = resources.getIdentifier(
        "status_bar_height", "dimen", "android"
    )
    if (idStatusBarHeight > 0)
        height = resources.getDimensionPixelSize(idStatusBarHeight)

    return height
}


fun hideViews(vararg views: View) {
    views.forEach {
        it.isEnabled = false
        it.visibility = View.INVISIBLE
    }
}

fun showViews(vararg views: View) {
    views.forEach {
        it.isEnabled = true
        it.visibility = View.VISIBLE
    }
}