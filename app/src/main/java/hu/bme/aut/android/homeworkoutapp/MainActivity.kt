package hu.bme.aut.android.homeworkoutapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.homeworkoutapp.databinding.ActivityMainBinding
import hu.bme.aut.android.homeworkoutapp.ui.welcome.WelcomeFragment
import hu.bme.aut.android.homeworkoutapp.ui.welcome.WelcomeFragmentDirections
import hu.bme.aut.android.homeworkoutapp.utils.Credentials

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_workouts, R.id.navigation_exercises, R.id.navigation_plans))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_main_settings -> {

                // TODO show a popup menu, or a dialog fragment
                // TODO sign out egy külön fragmenten belül majd, és ezt is kiszervezni a UserInteractorba
                FirebaseAuth.getInstance().signOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Credentials.serverClientId)
                    .requestEmail()
                    .build()
                val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                mGoogleSignInClient?.signOut()

                val action = WelcomeFragmentDirections.actionGlobalNavigationWelcome()
                findNavController(R.id.nav_host_fragment).navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun setToolbarAndBottomNavigationViewVisible(b: Boolean) {
        if(b) {
            binding.toolbar.visibility = View.VISIBLE
            binding.navView.visibility = View.VISIBLE
        }
        else {
            binding.toolbar.visibility = View.GONE
            binding.navView.visibility = View.GONE
        }

    }

}