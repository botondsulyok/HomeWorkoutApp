package hu.bme.aut.android.homeworkoutapp.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWelcomeBinding
import hu.bme.aut.android.homeworkoutapp.utils.Credentials


class WelcomeFragment : Fragment() {

    companion object {
        const val RC_SIGN_IN_GOOGLE = 100
    }

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity

        mainActivity?.setToolbarAndBottomNavigationViewVisible(false)
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null) {
            val action = WelcomeFragmentDirections.actionLogin()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignInWithGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Credentials.serverClientId)
                .requestEmail()
                .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(activity, "Logging in...", Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account?.idToken.toString())
            } catch (e: ApiException) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                mainActivity?.setToolbarAndBottomNavigationViewVisible(true)
                val action = WelcomeFragmentDirections.actionLogin()
                findNavController().navigate(action)
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
            }
    }



}