package com.wingspan.adminpanel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wingspan.adminpanel.activity.CategoriesActivity
import com.wingspan.adminpanel.databinding.FragmentCategories1Binding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener


class CategoriesFragment : Fragment() {

    lateinit var _binding: FragmentCategories1Binding
    val binding get()=_binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentCategories1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()

    }
    private fun setUI(){
        binding.apply {
            categories.setDebouncedClickListener(){
                val intent= Intent(requireContext(),CategoriesActivity::class.java)
                startActivity(intent)
            }
            subCategories.setDebouncedClickListener {

            }
        }
    }
}