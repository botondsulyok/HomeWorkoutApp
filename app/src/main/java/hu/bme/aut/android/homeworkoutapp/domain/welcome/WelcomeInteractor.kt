package hu.bme.aut.android.homeworkoutapp.domain.welcome

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WelcomeInteractor @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()

    fun loginWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure("Authentication failed")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}