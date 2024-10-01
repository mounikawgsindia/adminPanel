package com.wingspan.adminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.FlashSaleAprovalsAdapter
import com.wingspan.adminpanel.adapter.MerchantAdapter
import com.wingspan.adminpanel.databinding.ActivityFlashSaleApprovalsBinding
import com.wingspan.adminpanel.databinding.ActivityMainBinding

class FlashSaleApprovalsActivity : AppCompatActivity() {

    lateinit var _binding: ActivityFlashSaleApprovalsBinding
    val binding get()=_binding
    lateinit var flashSaleAdapter: FlashSaleAprovalsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding=ActivityFlashSaleApprovalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()
    }

    private fun setViewPager(){
        binding.apply {
            flashSaleAdapter= FlashSaleAprovalsAdapter(this@FlashSaleApprovalsActivity)
            viewPager2.adapter=flashSaleAdapter


            TabLayoutMediator(tabLayout,viewPager2){tab,position->
                tab.text= when(position){
                    0->"AWAITING"
                    1->"APPROVALS"
                    2->"REJECTION"
                    else->null
                }
            }.attach()
        }
    }
}