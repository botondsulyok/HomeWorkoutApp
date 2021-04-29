package hu.bme.aut.android.homeworkoutapp.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import hu.bme.aut.android.homeworkoutapp.core.WorkoutTestApplication


class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(cl, WorkoutTestApplication::class.java.name, context)
    }

}