package com.ersiver.filmflop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.preference.PreferenceManager
import com.ersiver.filmflop.databinding.ActivityMainBinding
import com.ersiver.filmflop.util.PREF_MODE_KEY
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        navView = binding.navView
        drawerLayout = binding.drawerLayout


        onDestinationChanged(toolbar)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        /**
         * Ensures that the SharedPreferences file is
         * properly initialized with the default values
         * when this method is called for the first time.
         */
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        setupMode()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, drawerLayout)
    }

    /**
     * The default toolbar is hidden from the HomeFragment,
     * SearchFragment and DetailFragment since there are
     * customized toolbars in those fragments.
     */
    private fun onDestinationChanged(toolbar: Toolbar) {
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.nav_detail -> toolbar.visibility = View.GONE
                R.id.nav_home -> toolbar.visibility = View.GONE
                R.id.nav_search -> toolbar.visibility = View.GONE
                else -> toolbar.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Setup night mode.
     */
    private fun setupMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isNightMode = sharedPreferences.getBoolean(PREF_MODE_KEY, false)

        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

