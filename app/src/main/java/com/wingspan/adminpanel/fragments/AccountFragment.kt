package com.wingspan.adminpanel.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.activity.AccountSettingsActivity
import com.wingspan.adminpanel.databinding.FragmentAccountBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener


class AccountFragment : Fragment() {
    var _binding: FragmentAccountBinding?=null
    val binding get()=_binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentAccountBinding.inflate(inflater,container,false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Extensions.setStatusBarColor(requireActivity(), R.color.white)
        setUI()
    }
    private fun setUI(){
        binding?.apply {
            accountSettings.setDebouncedClickListener(){
                val intent= Intent(requireActivity(), AccountSettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}