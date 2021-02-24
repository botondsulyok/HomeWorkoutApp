package hu.bme.aut.android.homeworkoutapp.ui.welcome

sealed class WelcomeViewState

object LoggedOut : WelcomeViewState()

object LoggingIn : WelcomeViewState()

data class LoginFailed(val message: String) : WelcomeViewState()

object LoggedIn : WelcomeViewState()