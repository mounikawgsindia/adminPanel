package com.wingspan.adminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.wingspan.adminpanel.databinding.ActivityAccountSettingsBinding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.utils.UserPreferences


class AccountSettingsActivity : AppCompatActivity() {
    lateinit var _binding: ActivityAccountSettingsBinding
    val binding get()=_binding
    lateinit var alertDialog:AlertDialog
    lateinit var sharedPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
    }
    fun setUI(){
        binding.apply {
            logout.setDebouncedClickListener(){
                showAlertDialog()
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun showAlertDialog(){
        val builder= AlertDialog.Builder(this@AccountSettingsActivity)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure want to Logout?")
        builder.setPositiveButton("OK"){dialog,which->
            sharedPreferences= UserPreferences(this@AccountSettingsActivity)
            sharedPreferences.logoutShopKeeper()
            val intent= Intent(this@AccountSettingsActivity, LoginActivity::class.java)
            startActivity(intent)
           finish()

        }
        builder.setNegativeButton("CANCEL"){dialog,which->
            dialog.dismiss()
        }
        alertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}