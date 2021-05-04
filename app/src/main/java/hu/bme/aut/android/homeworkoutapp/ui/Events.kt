package hu.bme.aut.android.homeworkoutapp.ui

import co.zsmb.rainbowcake.base.OneShotEvent


class UploadFailed(val message: String = "") : OneShotEvent

class UploadSuccess(val message: String = "") : OneShotEvent

class ActionFailed(val message: String = "") : OneShotEvent

class ActionSuccess(val message: String = "") : OneShotEvent

