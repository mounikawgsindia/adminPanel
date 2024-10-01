package com.wingspan.adminpanel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingspan.adminpanel.databinding.FragmentCategoriesBinding


class CategoriesFragment : Fragment() {
var _binding: FragmentCategoriesBinding?=null
    val binding get()=_binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentCategoriesBinding.inflate(layoutInflater)
        return binding?.root
    }


}