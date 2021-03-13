package hu.bme.aut.android.homeworkoutapp.utils

data class Duration(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
) {
    companion object {
        val ZERO = Duration(0, 0, 0)
    }
}
