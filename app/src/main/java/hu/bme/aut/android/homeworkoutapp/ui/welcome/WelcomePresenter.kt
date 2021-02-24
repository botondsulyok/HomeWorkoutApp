package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.withIOContext
import com.google.firebase.auth.AuthCredential
import hu.bme.aut.android.homeworkoutapp.domain.welcome.WelcomeInteractor
import javax.inject.Inject

class WelcomePresenter @Inject constructor(
    private val welcomeInteractor: WelcomeInteractor
) {

    suspend fun loginWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) = withIOContext {
        welcomeInteractor.loginWithGoogle(credential, onSuccess, onFailure)
    }

    suspend fun getCurrentUser() = withIOContext {
        welcomeInteractor.getCurrentUser()
    }

}