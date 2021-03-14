package hu.bme.aut.android.homeworkoutapp.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.children
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.setAllEnabled(enabled: Boolean) {
    isEnabled = enabled
    (this as? ViewGroup)?.children?.forEach {
            child -> child.setAllEnabled(enabled)
    }
}

fun Editable?.toInt() = this.toString().toInt()