package hu.bme.aut.android.homeworkoutapp.ui.welcome

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWelcomeBinding
import hu.bme.aut.android.homeworkoutapp.utils.Credentials


class WelcomeFragment : RainbowCakeFragment<WelcomeViewState, WelcomeViewModelBase>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

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
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUserSignedIn()
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
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

            val mGoogleSignInClient = mainActivity?.let { it1 -> GoogleSignIn.getClient(it1, gso) }

            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken.toString(), null)
                viewModel.signInWithGoogle(credential)
            } catch (e: ApiException) {
                Log.e("Google Auth Error", e.message.toString())
            }
        }

    }

    override fun render(viewState: WelcomeViewState) {
        binding.progressBarWelcome.visibility = View.GONE
        binding.btnSignInWithGoogle.isEnabled = true
        when(viewState) {
            is Loading -> {
                binding.ivLoading.visibility = View.VISIBLE
            }
            is SignedOut -> {
                mainActivity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                mainActivity?.setToolbarAndBottomNavigationViewVisible(false)
                binding.ivLoading.visibility = View.GONE
            }
            is SigningIn -> {
                binding.progressBarWelcome.visibility = View.VISIBLE
                binding.btnSignInWithGoogle.isEnabled = false
                binding.ivLoading.visibility = View.GONE
            }
            is SignInFailed -> {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.title_error))
                    .setMessage(viewState.message)
                    .setNeutralButton(getString(R.string.btn_ok), null)
                    .show()
                binding.ivLoading.visibility = View.GONE
            }
            is SignedIn -> {
                mainActivity?.setToolbarAndBottomNavigationViewVisible(true)
                val action = WelcomeFragmentDirections.actionLogin()
                findNavController().navigate(action)
            }
        }.exhaustive
    }


}