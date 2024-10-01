package com.wingspan.adminpanel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.MerchantApprovalAdapter
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding


class MerchantApprovals : Fragment() {
    lateinit var _binding: FragmentMerchantApprovalsBinding
    val binding get()=_binding
    private lateinit var merchantAdapter:MerchantApprovalAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMerchantApprovalsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    private fun setViewPager(){
        binding.apply {
            merchantAdapter= MerchantApprovalAdapter(requireActivity())
            viewpager.adapter=merchantAdapter


            TabLayoutMediator(tabLayout,viewpager){tab,position->
                tab.text= when(position){
                    0->"REQUESTS"
                    1->"APPROVALS"
                    2->"NON APPROVALS"
                    else->null
                }
            }.attach()
        }
    }
}