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

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding
    val binding get()=_binding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Safely get the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found")

        val navController: NavController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        binding.bottomNavigation.setupWithNavController(navController)
        val menuItem=findViewById<BottomNavigationView>(R.id.bottom_navigation).getChildAt(0)as BottomNavigationMenuView
        val itemView =menuItem.getChildAt(1) as BottomNavigationItemView
        val badgeView=LayoutInflater.from(this@MainActivity).inflate(R.layout.custom_badge,itemView,false)
        itemView.addView(badgeView)
        val badgeText=badgeView.findViewById<TextView>(R.id.badge_text)
        badgeText.text="7"
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

                R.id.account -> {
                    navController.navigate(R.id.account)
                    true
                }
                else -> false
            }
        }
    }


}