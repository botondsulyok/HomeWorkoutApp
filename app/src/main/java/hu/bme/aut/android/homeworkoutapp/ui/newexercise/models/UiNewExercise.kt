package hu.bme.aut.android.homeworkoutapp.ui.newexercise.models

data class UiNewExercise(
        val name: String = "",
        val isRepsAdded: Boolean = true,
        val repsOrDurationValue: Int = 0
)