package hu.bme.aut.android.homeworkoutapp.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Duration(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) : Parcelable {

    companion object {
        val ZERO = Duration(0, 0, 0)

        fun build(durationInSeconds: Int): Duration {
            val hours: Int = durationInSeconds / 3600
            val secondsLeft: Int = durationInSeconds - hours * 3600
            val minutes: Int = secondsLeft / 60
            val seconds: Int = secondsLeft - minutes * 60
            return Duration(hours, minutes, seconds)
        }
    }

    fun getDurationInSeconds(): Int {
        return hours * 3600 + minutes * 60 + seconds
    }

}
