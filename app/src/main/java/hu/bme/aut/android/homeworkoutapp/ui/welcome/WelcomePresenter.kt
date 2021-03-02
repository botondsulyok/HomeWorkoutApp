package hu.bme.aut.android.homeworkoutapp.ui.welcome

import co.zsmb.rainbowcake.withIOContext
import com.google.firebase.auth.AuthCredential
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.domain.interactors.UserInteractor
import java.lang.Exception
import javax.inject.Inject

class WelcomePresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun signInWithGoogle(
        credential: AuthCredential
    ): Result<Unit, Exception> = withIOContext {
        userInteractor.signInWithGoogle(credential)
    }

    suspend fun getCurrentUser() = withIOContext {
        userInteractor.getCurrentUser()
    }

}