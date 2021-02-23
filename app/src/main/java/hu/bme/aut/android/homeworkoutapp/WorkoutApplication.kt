package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import hu.bme.aut.android.homeworkoutapp.di.DaggerAppComponent
import hu.bme.aut.android.homeworkoutapp.di.AppComponent

class WorkoutApplication : RainbowCakeApplication() {

    override lateinit var injector: AppComponent

    override fun setupInjector() {
        injector = DaggerAppComponent.create()
    }


}