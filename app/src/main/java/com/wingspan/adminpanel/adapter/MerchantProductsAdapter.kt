package com.wingspan.adminpanel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wingspan.adminpanel.fragments.CategoriesFragment
import com.wingspan.adminpanel.fragments.OffersFragment

class MerchantProductsAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    // Number of tabs
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OffersFragment()
            1 -> CategoriesFragment()
            else -> OffersFragment()
        }
    }
}