package com.example.myapplication

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.LocalStorage.RoomService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.proximite_fragment.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Context for RoomService
        RoomService.context = this

        // NavController
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        // Setup BottomNav to navigate with navController
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            when(dest){
                "com.example.myapplication:id/login" ->{
                    bottom_nav_view.visibility = View.GONE
                }

                "com.example.myapplication:id/commandes" ->{
                    bottom_nav_view.visibility = View.VISIBLE
                }

                "com.example.myapplication:id/villes" ->{
                    bottom_nav_view.visibility = View.VISIBLE
                }

                "com.example.myapplication:id/pharmacie" -> {
                    bottom_nav_view.visibility = View.GONE
                }
            }
        }

    }



    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }
}
