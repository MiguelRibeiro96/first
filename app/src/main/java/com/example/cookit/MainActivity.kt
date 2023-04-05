package com.example.cookit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cookit.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mNavController:NavController
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarViews.toolbarHome)

        setupNavDrawer()

        mAuth = FirebaseAuth.getInstance()

    }

    private fun setupNavDrawer(){

        val navView = binding.navView
        val navDrawer = binding.navView


        val headerView = navView.getHeaderView(0) // Get reference to header view

        // Access emailTextView in headerView layout
        val emailTextView = headerView.findViewById<TextView>(R.id.emailTextView)

        // Set text of emailTextView based on current user
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            emailTextView.text = email
        } else {
            emailTextView.text = "No user logged in"
        }

        val drawerLayout = binding.navDrawerLayout
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment

        mNavController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.favoritesFragment, R.id.profileFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(mNavController,appBarConfiguration)
        navDrawer.setupWithNavController(mNavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}