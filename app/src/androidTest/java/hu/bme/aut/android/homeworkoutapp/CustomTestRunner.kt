package hu.bme.aut.android.homeworkoutapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner


class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(cl, WorkoutTestApplication::class.java.name, context)
    }

}