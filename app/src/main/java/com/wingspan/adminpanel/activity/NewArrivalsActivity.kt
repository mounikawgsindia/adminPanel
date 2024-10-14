package com.wingspan.adminpanel.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.wingspan.adminpanel.adapter.FlashSaleAprovalsAdapter
import com.wingspan.adminpanel.adapter.NewArrivalsAdapter
import com.wingspan.adminpanel.databinding.ActivityFlashSaleApprovalsBinding
import com.wingspan.adminpanel.databinding.ActivityNewArrivalsBinding

class NewArrivalsActivity: AppCompatActivity()  {
    lateinit var _binding:ActivityNewArrivalsBinding
    val binding get()=_binding
    lateinit var flashSaleAdapter: NewArrivalsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding= ActivityNewArrivalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()
    }

    private fun setViewPager(){
        binding.apply {
            flashSaleAdapter= NewArrivalsAdapter(this@NewArrivalsActivity)
            viewpagerNewArrivals.adapter=flashSaleAdapter


            TabLayoutMediator(tabLayoutFlashsale,viewpagerNewArrivals){tab,position->
                tab.text= when(position){
                    0->"AWAITING"
                    1->"APPROVALS"
                    2->"REJECTION"
                    else->null
                }
            }.attach()

            backIv.setOnClickListener{
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}