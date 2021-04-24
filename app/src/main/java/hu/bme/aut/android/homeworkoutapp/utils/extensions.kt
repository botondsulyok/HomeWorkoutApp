package hu.bme.aut.android.homeworkoutapp.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.children
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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

fun Editable?.toInt(): Int {
    return if(this == null || this.isEmpty()) {
        0
    } else {
        this.toString().toInt()
    }
}


fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun Date.toDateStr(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.US).format(this)
}

fun Date.toYearStr(): String {
    return SimpleDateFormat("yyyy", Locale.US).format(this)
}

fun Date.toMonthStr(): String {
    return SimpleDateFormat("MMM", Locale.US).format(this)
}

fun String.toDate(): Date {
    return SimpleDateFormat("dd MMM yyyy", Locale.US).parse(this)
}