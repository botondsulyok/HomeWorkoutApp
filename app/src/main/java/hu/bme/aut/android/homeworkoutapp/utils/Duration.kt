package hu.bme.aut.android.homeworkoutapp.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Duration(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) : Parcelable {
    companion object {
        val ZERO = Duration(0, 0, 0)
    }
}
