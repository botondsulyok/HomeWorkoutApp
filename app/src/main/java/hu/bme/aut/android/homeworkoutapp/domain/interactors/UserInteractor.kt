package hu.bme.aut.android.homeworkoutapp.domain.interactors

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.homeworkoutapp.data.firebase.FireBaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val fireBaseDataSource: FireBaseDataSource
    ) {

    private val auth = FirebaseAuth.getInstance()

    suspend fun signInWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        fireBaseDataSource.signInWithGoogle(credential, onSuccess, onFailure)
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return fireBaseDataSource.getCurrentUser()
    }

    suspend fun signOut() {
        fireBaseDataSource.signOut()
    }

}