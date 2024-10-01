package com.wingspan.adminpanel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingspan.adminpanel.activity.FlashSaleApprovalsActivity
import com.wingspan.adminpanel.databinding.FragmentOffersBinding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener



class OffersFragment : Fragment() {

    var _binding: FragmentOffersBinding?=null
    val binding get()=_binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentOffersBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setObservers()
        setRecycleView()
    }
    private fun setUI(){
        binding?.apply {
            flashsale.setDebouncedClickListener(){
                val intent= Intent(requireActivity(), FlashSaleApprovalsActivity::class.java)
                startActivity(intent)
            }

        }
    }
    private fun setObservers(){

    }
    private fun setRecycleView(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}