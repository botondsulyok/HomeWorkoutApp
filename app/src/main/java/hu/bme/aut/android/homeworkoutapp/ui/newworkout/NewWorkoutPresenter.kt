package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.domain.interactors.NewWorkoutInteractor
import javax.inject.Inject

class NewWorkoutPresenter @Inject constructor(
	private val blankInteractor: NewWorkoutInteractor) {

    suspend fun getData(): String = withIOContext {
        ""
    }

}