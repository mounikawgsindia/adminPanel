package com.wingspan.adminpanel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wingspan.adminpanel.databinding.ActivityMainBinding
import com.wingspan.adminpanel.extensions.Extensions

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding
    val binding get()=_binding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  credentials
        val apiKey = "jyoshna"
        val apiSecret = "123456789"

        // Store credentials securely
        Extensions.storeAuthCredentials(this, apiKey, apiSecret)
        // Retrieve credentials securely
        val data= Extensions.getAuthHeaderValues(this)
        // Use the credentials as needed
        println("Stored API Key: $data")
        // Safely get the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found")

        val navController: NavController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.home)
                    true
                }
                R.id.merchant_verify -> {
                    navController.navigate(R.id.merchant_verify)
                    true
                }
                R.id.prod_approval -> {
                    navController.navigate(R.id.prod_approval)
                    true
                }
                R.id.stock -> {
                    navController.navigate(R.id.stock)
                    true
                }
                R.id.account -> {
                    navController.navigate(R.id.account)
                    true
                }
                else -> false
            }
        }
    }


}