package hu.bme.aut.android.homeworkoutapp.ui.plans.models

import java.time.LocalDate

data class UiEvent(
    val id: String = "",
    val date: LocalDate = LocalDate.now()
)
