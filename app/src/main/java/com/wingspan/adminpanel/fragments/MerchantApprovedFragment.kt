package com.wingspan.adminpanel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingspan.adminpanel.databinding.FragmentMerchantBinding


class MerchantApprovedFragment : Fragment() {

    lateinit var _binding: FragmentMerchantBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantBinding.inflate(inflater, container, false)
        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }
    private fun setUI(){

    }
    override fun onDestroyView() {
        super.onDestroyView()

    }
}