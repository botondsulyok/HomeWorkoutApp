package hu.bme.aut.android.homeworkoutapp.utils

import android.content.Context
import javax.inject.Inject

class ResourcesHelper @Inject constructor(private val context: Context) {

    fun getString(resId: Int) = context.getString(resId)

}