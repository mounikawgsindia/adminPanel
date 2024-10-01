package com.wingspan.adminpanel.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel

class HomeFragment : Fragment() {
    lateinit var _binding: FragmentMerchantApprovalsBinding
    val binding get()=_binding
    private val viewModel:ShopKeeperViewModel by viewModels()

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
        fetchDataFromNetwork()
        setUI()
        setObserver()
        setRecycleView()
    }
    private fun setUI(){

    }
    private fun setObserver(){



    }
    private fun fetchDataFromNetwork(){
        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.approvedMerchatApi()
        }else{
            Extensions.showNetworkAlertDialog(requireActivity())
        }
    }
    private fun setRecycleView(){

    }
}