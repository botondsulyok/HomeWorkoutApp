package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import com.google.firebase.auth.AuthCredential
import hu.bme.aut.android.homeworkoutapp.ui.welcome.WelcomeViewModelBase
import javax.inject.Inject

class TestWelcomeViewModel @Inject constructor() : WelcomeViewModelBase() {

    override fun checkUserSignedIn() {

    }

    override fun signInWithGoogle(credential: AuthCredential) {

    }

}