package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.withIOContext
import com.google.firebase.auth.AuthCredential
import hu.bme.aut.android.homeworkoutapp.domain.interactors.UserInteractor
import javax.inject.Inject

class WelcomePresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun signInWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) = withIOContext {
        userInteractor.signInWithGoogle(credential, onSuccess, onFailure)
    }

    suspend fun getCurrentUser() = withIOContext {
        userInteractor.getCurrentUser()
    }

}