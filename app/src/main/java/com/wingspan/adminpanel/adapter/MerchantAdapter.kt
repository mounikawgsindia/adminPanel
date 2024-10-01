package com.wingspan.adminpanel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wingspan.adminpanel.fragments.MerchantApprovedFragment
import com.wingspan.adminpanel.fragments.MerchantRejections
import com.wingspan.adminpanel.fragments.MerchatAwaitingFragment

class MerchantAdapter(fragmentActivity:FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->MerchatAwaitingFragment()
            1->MerchantApprovedFragment()
            2->MerchantRejections()
            else->MerchatAwaitingFragment()
        }
    }
}