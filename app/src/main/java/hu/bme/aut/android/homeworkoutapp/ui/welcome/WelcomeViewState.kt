package hu.bme.aut.android.homeworkoutapp.ui.welcome

sealed class WelcomeViewState

object SignedOut : WelcomeViewState()

object SigningIn : WelcomeViewState()

data class SignInFailed(val message: String) : WelcomeViewState()

object SignedIn : WelcomeViewState()