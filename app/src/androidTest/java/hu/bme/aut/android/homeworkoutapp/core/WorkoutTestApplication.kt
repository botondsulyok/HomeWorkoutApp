package hu.bme.aut.android.homeworkoutapp.core

import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import hu.bme.aut.android.homeworkoutapp.core.di.DaggerTestAppComponent
import hu.bme.aut.android.homeworkoutapp.di.ApplicationModule

class WorkoutTestApplication : RainbowCakeApplication() {

    override lateinit var injector: RainbowCakeComponent

    override fun setupInjector() {
        injector = DaggerTestAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }


}