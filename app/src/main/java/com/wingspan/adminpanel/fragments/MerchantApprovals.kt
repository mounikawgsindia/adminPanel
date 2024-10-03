package com.wingspan.adminpanel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.MerchantAdapter
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding
import com.wingspan.adminpanel.databinding.FragmentMerchantBinding
import com.wingspan.adminpanel.extensions.Extensions


class MerchantApprovals : Fragment() {
    lateinit var _binding: FragmentMerchantBinding
    val binding get()=_binding
    private lateinit var merchantAdapter:MerchantAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMerchantBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Extensions.setStatusBarColor(requireActivity(), R.color.green)
        setViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    private fun setViewPager(){
        binding.apply {
            merchantAdapter= MerchantAdapter(requireActivity())
            viewpager.adapter=merchantAdapter


            TabLayoutMediator(tabLayout,viewpager){tab,position->
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