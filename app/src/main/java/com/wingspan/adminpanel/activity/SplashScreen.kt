package com.wingspan.adminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.wingspan.adminpanel.MainActivity
import com.wingspan.adminpanel.databinding.ActivitySplashScreenBinding
import com.wingspan.adminpanel.utils.UserPreferences


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    lateinit var _binding: ActivitySplashScreenBinding
    val binding get()=_binding
    lateinit var sharedPreferences: UserPreferences
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= UserPreferences(this@SplashScreen)
        Log.d("logout check","log out check ${sharedPreferences.isLoggedIn()}")
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferences.isLoggedIn())
            {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                finish()
            }

        },2000)


    }
}