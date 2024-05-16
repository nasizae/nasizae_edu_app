package com.example.nasizae_edu_pulse

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.edupulse.data.pref.Pref
import com.example.nasizae_edu_pulse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pref: Pref by lazy {
        Pref(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_personal_library,
                R.id.navigation_sittings,
                R.id.navigation_static,
                R.id.navigation_chat
            )
        )
        val fragmentWithOutButtonNav=setOf(
            R.id.onBoardingFragment,
            R.id.authenticationFragment,
            R.id.registrationFragment,
        )
        supportActionBar?.hide()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destination,_->
            if (fragmentWithOutButtonNav.contains(destination.id)){
                navView.isVisible=false
            }
            else{
                navView.isVisible=true
            }
        }
    }
}