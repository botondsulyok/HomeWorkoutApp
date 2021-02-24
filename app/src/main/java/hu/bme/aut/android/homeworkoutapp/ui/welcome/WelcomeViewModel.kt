package hu.bme.aut.android.homeworkoutapp.ui.welcome

import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val welcomePresenter: WelcomePresenter
) : RainbowCakeViewModel<WelcomeViewState>(LoggedOut) {

    fun checkUserLoggedIn() = execute {
        viewState = LoggingIn
        viewState = if(welcomePresenter.getCurrentUser() != null) {
            LoggedIn
        } else {
            LoggedOut
        }
    }

    fun loginWithGoogle(credential: AuthCredential) = execute {
        viewState = LoggingIn

        welcomePresenter.loginWithGoogle(
            credential,
            { viewState = LoggedIn },
            { viewState = LoginFailed(it) }
        )

    }

}