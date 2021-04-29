package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import hu.bme.aut.android.homeworkoutapp.di.ApplicationModule
import hu.bme.aut.android.homeworkoutapp.di.DaggerTestAppComponent

class WorkoutTestApplication : RainbowCakeApplication() {

    override lateinit var injector: RainbowCakeComponent

    override fun setupInjector() {
        injector = DaggerTestAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }


}