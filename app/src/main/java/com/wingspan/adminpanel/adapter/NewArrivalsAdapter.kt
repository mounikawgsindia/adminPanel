package com.wingspan.adminpanel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wingspan.adminpanel.fragments.FlashSaleApprovalFragment
import com.wingspan.adminpanel.fragments.FlashSaleAwaitingFragment
import com.wingspan.adminpanel.fragments.FlashSaleRejectFragment
import com.wingspan.adminpanel.fragments.MerchatAwaitingFragment
import com.wingspan.adminpanel.fragments.NewArrivalsApprovalFragment
import com.wingspan.adminpanel.fragments.NewArrivalsAwaitingFragment
import com.wingspan.adminpanel.fragments.NewArrivalsRejectFragment

class NewArrivalsAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> NewArrivalsAwaitingFragment()
            1-> NewArrivalsApprovalFragment()
            2-> NewArrivalsRejectFragment()
            else-> NewArrivalsAwaitingFragment()
        }
    }
}