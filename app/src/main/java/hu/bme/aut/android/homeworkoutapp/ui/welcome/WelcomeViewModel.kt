package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val welcomePresenter: WelcomePresenter
) : RainbowCakeViewModel<WelcomeViewState>(Loading) {

    fun checkUserSignedIn() = execute {
        viewState = if(welcomePresenter.getCurrentUser() != null) {
            SignedIn
        } else {
            SignedOut
        }
    }

    fun signInWithGoogle(credential: AuthCredential) = execute {
        viewState = SigningIn

        welcomePresenter.signInWithGoogle(
            credential,
            { viewState = SignedIn },
            { viewState = SignInFailed(it) }
        )

    }

}