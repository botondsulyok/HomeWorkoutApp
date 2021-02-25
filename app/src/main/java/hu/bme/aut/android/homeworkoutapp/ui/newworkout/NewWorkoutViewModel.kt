package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
	import javax.inject.Inject
	
	class NewWorkoutViewModel @Inject constructor(
    private val newWorkoutPresenter: NewWorkoutPresenter
) : RainbowCakeViewModel<NewWorkoutViewState>(Ready) {

    fun upload() {

    }

}