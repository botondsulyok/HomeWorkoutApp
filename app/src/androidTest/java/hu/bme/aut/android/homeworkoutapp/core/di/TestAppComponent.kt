package hu.bme.aut.android.homeworkoutapp.core.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.android.homeworkoutapp.di.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RainbowCakeModule::class,
        TestViewModelModule::class,
        ApplicationModule::class
    ]
)
interface TestAppComponent : RainbowCakeComponent