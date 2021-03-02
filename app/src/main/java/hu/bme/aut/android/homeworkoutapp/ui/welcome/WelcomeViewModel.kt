package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.AuthCredential
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
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
        val result = welcomePresenter.signInWithGoogle(credential)
        viewState = when(result) {
            is ResultSuccess -> {
                SignedIn
            }
            is ResultFailure -> {
                SignInFailed(result.reason.message.toString())
            }
        }


    }

}