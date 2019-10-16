import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


/**
 * Created by Vladimir Kraev
 */

/**
 * The "fragment" is added to the container view with id "containerId". The operation is
 * performed by the "fragmentManager".
 */
fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment, @IdRes containerId: Int,
    addToStack: Boolean = true
) {
    supportFragmentManager.transact {
        if (addToStack) addToBackStack(null)
        replace(containerId, fragment)
    }
}

fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment, @IdRes containerId: Int,
    backstackTag: String?
) {
    supportFragmentManager.transact {
        addToBackStack(backstackTag)
        replace(containerId, fragment)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(funciton: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        funciton()
    }.commit()
}