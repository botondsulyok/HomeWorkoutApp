package hu.bme.aut.android.homeworkoutapp.ui

import co.zsmb.rainbowcake.base.OneShotEvent


data class UploadFailed(val message: String = "") : OneShotEvent

data class UploadSuccess(val message: String = "") : OneShotEvent

data class ActionFailed(val message: String = "") : OneShotEvent

data class ActionSuccess(val message: String = "") : OneShotEvent

