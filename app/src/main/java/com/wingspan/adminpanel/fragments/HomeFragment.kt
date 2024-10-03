package com.wingspan.adminpanel.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.databinding.FragmentHomeBinding
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel

class HomeFragment : Fragment() {
    lateinit var _binding: FragmentHomeBinding
    val binding get()=_binding
    private val viewModel:ShopKeeperViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()

    }
    private fun setUI(){
        binding?.apply {

            merchantLl.setDebouncedClickListener() {
                findNavController().navigate(R.id.merchant_verify)
            }
            prodLl.setDebouncedClickListener {
                findNavController().navigate(R.id.prod_approval)
            }
            stockDetails.setDebouncedClickListener {
                findNavController().navigate(R.id.stock)
            }
        }
    }

}