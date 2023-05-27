package kyrylost.apps.eatwise

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loggedInSharedPref = this.getSharedPreferences(
            "userLoggedInOnDevice", Context.MODE_PRIVATE
        ) ?: return

        val userIsLoggedIn = loggedInSharedPref.getBoolean("userIsLoggedIn", false)

        if (userIsLoggedIn) {
            val navHostFragment = supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment_container) as NavHostFragment
            val graphInflater = navHostFragment.navController.navInflater
            navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = navHostFragment.navController

            navGraph.setStartDestination(R.id.consumedFragment)
            navController.graph = navGraph
        }

//        if (userIsLoggedIn) {
//            // try to login user with needed data show progress bar while signing in
//        }
//        else {
//
//        }
        // Check, if account was created or user were login.
        // If Yes, try to login user with needed data show progress bar while signing in,
            // if success show data from room, else show error, add retry button
        //If No show first launch fragment

    }
}