package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import hu.bme.aut.android.homeworkoutapp.di.ApplicationModule
import hu.bme.aut.android.homeworkoutapp.di.DaggerAppComponent

class WorkoutApplication : RainbowCakeApplication() {

    override lateinit var injector: RainbowCakeComponent

    override fun setupInjector() {
        injector = DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }


}