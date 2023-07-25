package kyrylost.apps.eatwise

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kyrylost.apps.eatwise.reseter.scheduleDatabaseResetAtSpecificTime
import kyrylost.apps.eatwise.viewmodel.UserViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph
    private lateinit var navBar: BottomNavigationView
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scheduleDatabaseResetAtSpecificTime(applicationContext)

        navBar = findViewById(R.id.navBar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val loggedInSharedPref = this.getSharedPreferences(
            "userLoggedInOnDevice", Context.MODE_PRIVATE
        ) ?: return
        val emailSharedPref = this.getSharedPreferences(
            "email", Context.MODE_PRIVATE
        ) ?: return
        val passwordSharedPref = this.getSharedPreferences(
            "password", Context.MODE_PRIVATE
        ) ?: return

        val userIsLoggedIn = loggedInSharedPref.getBoolean("userIsLoggedIn", false)

        if (userIsLoggedIn) {
            val email = emailSharedPref.getString("email", "")!!
            val password = passwordSharedPref.getString("password", "")!!

            userViewModel.setEmailAndPassword(email, password)
            userViewModel.login()

            val graphInflater = navHostFragment.navController.navInflater
            navGraph = graphInflater.inflate(R.navigation.nav_graph)

            navGraph.setStartDestination(R.id.consumedFragment)
            navController.graph = navGraph
        }

        navBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.consumedFragment -> showBottomNav()
                R.id.foodListFragment -> showBottomNav()
                R.id.personalPageFragment -> showBottomNav()
                R.id.favoriteFoodFragment -> showBottomNav()
                R.id.ownFoodFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

    }

    private fun showBottomNav() {
        navBar.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        navBar.visibility = View.GONE

    }

}