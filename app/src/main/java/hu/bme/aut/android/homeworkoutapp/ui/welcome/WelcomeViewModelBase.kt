package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.AuthCredential

abstract class WelcomeViewModelBase : RainbowCakeViewModel<WelcomeViewState>(Loading) {
    abstract fun checkUserSignedIn()

    abstract fun signInWithGoogle(credential: AuthCredential)
}