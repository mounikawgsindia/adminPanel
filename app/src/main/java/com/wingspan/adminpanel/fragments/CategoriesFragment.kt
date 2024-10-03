package com.wingspan.adminpanel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.CategoriesAdapter
import com.wingspan.adminpanel.databinding.FragmentCategoriesBinding
import com.wingspan.adminpanel.model.CategoriesModel


class CategoriesFragment : Fragment() {
    var _binding: FragmentCategoriesBinding?=null
    val binding get()=_binding
    lateinit var categoryAdapter:CategoriesAdapter
    val categoryList=ArrayList<CategoriesModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setRecycleView()
    }
    private fun setUI(){
        binding.apply {

        }
    }
    private fun setRecycleView(){
        binding?.categoryRv?.apply {
            categoryAdapter= CategoriesAdapter(requireContext(),categoryList)
            layoutManager= GridLayoutManager(requireContext(),4, GridLayoutManager.VERTICAL,false)
            adapter=categoryAdapter
            getCategoriesData()
        }

    }
    private fun getCategoriesData(){
        categoryList.clear()
        val data= arrayListOf(
            CategoriesModel(R.drawable.cat1,"Dailry & Cheese"),
            CategoriesModel(R.drawable.cat2,"Cold pressed juice"),
            CategoriesModel(R.drawable.cat3,"Fruits and vegitables"),
            CategoriesModel(R.drawable.cat4,"Fish and Sea Food"),
            CategoriesModel(R.drawable.cat5,"Prawns"),
            CategoriesModel(R.drawable.cat6,"Poultry"),
            CategoriesModel(R.drawable.cat7,"Mutton"),
            CategoriesModel(R.drawable.cat8,"Steaks & Fillets"),
            CategoriesModel(R.drawable.cat9,"Bone less Fillets"),
            CategoriesModel(R.drawable.cat10,"Eggs"),
            CategoriesModel(R.drawable.cat11,"Batters & Porota"),
            CategoriesModel(R.drawable.cat12,"Ready To cook"),

        )
        categoryList.addAll(data)

        categoryAdapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }



}