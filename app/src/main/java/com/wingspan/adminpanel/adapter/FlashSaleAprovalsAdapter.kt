package com.wingspan.adminpanel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wingspan.adminpanel.fragments.FlashSaleApprovalFragment
import com.wingspan.adminpanel.fragments.FlashSaleAwaitingFragment
import com.wingspan.adminpanel.fragments.FlashSaleRejectFragment
import com.wingspan.adminpanel.fragments.MerchantApprovedFragment
import com.wingspan.adminpanel.fragments.MerchantRejections
import com.wingspan.adminpanel.fragments.MerchatAwaitingFragment

class FlashSaleAprovalsAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FlashSaleAwaitingFragment()
            1-> FlashSaleApprovalFragment()
            2-> FlashSaleRejectFragment()
            else-> MerchatAwaitingFragment()
        }
    }
}