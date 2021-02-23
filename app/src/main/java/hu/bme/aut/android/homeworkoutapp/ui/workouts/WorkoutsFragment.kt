package hu.bme.aut.android.homeworkoutapp.ui.workouts

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.R
import kotlinx.android.synthetic.main.fragment_workouts.*

class WorkoutsFragment : RainbowCakeFragment<WorkoutsViewState, WorkoutsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_workouts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views

    }

    override fun onStart() {
        super.onStart()
        viewModel.loadWorkouts()
    }

    override fun render(viewState: WorkoutsViewState) {
        when(viewState) {
            is WorkoutsLoading -> {
                progressBar.visibility = View.VISIBLE
            }
            is WorkoutsReady -> {
                progressBar.visibility = View.GONE
            }
        }.exhaustive

    }

}