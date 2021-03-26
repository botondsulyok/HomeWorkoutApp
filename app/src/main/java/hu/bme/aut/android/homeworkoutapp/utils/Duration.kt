package hu.bme.aut.android.homeworkoutapp.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class Duration(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) : Parcelable {

    companion object {
        val ZERO = Duration(0, 0, 0)

        fun buildFromMilliseconds(durationInMilliseconds: Int): Duration {
            //val durationInSeconds = if(durationInMilliseconds / 1000 != 0) durationInMilliseconds / 1000 else 1
            val durationInSeconds = (durationInMilliseconds / 1000.0).roundToInt()
            val hours: Int = durationInSeconds / 3600
            val secondsLeft: Int = durationInSeconds - hours * 3600
            val minutes: Int = secondsLeft / 60
            val seconds: Int = secondsLeft - minutes * 60
            return Duration(hours, minutes, seconds)
        }

    }

    private fun getDurationInSeconds(): Int {
        return hours * 3600 + minutes * 60 + seconds
    }

    fun getDurationInMilliseconds(): Int {
        return getDurationInSeconds() * 1000
    }

    override fun toString(): String {
        fun formatNumber(number: Int): String {
            return if(number < 10) "0$number" else number.toString()
        }
        return "${if(formatNumber(hours) != "00") "${formatNumber(hours)}:" else ""}${formatNumber(minutes)}:${formatNumber(seconds)}"
    }

}
