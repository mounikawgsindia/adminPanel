package com.wingspan.adminpanel.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.MerchantPendingAdapter
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding
import com.wingspan.adminpanel.databinding.FragmentMerchantPendingListBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.PendingMerchants
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel
import com.wingspan.shopkeeper.api.ApiDataCache

class MerchatPendingFragment : Fragment() {
    lateinit var _binding: FragmentMerchantPendingListBinding
    val binding get()=_binding
    lateinit var maerchantAdapter:MerchantPendingAdapter
    var merchantList=ArrayList<PendingMerchants>()
    private val viewModel: ShopKeeperViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMerchantPendingListBinding.inflate(layoutInflater)
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
        viewModel.merchatPendingError.observe(viewLifecycleOwner){error->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.merchatPendingResponse.observe(viewLifecycleOwner){response->
            merchantList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            merchantList= response as ArrayList<PendingMerchants>
            if(merchantList.size==0){
                binding.pendingMerchantRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
            }
            else{
                binding.pendingMerchantRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                maerchantAdapter.setData(merchantList)
            }
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if(isLoading){

                binding.loader.visibility = View.GONE

            }else{
                binding.loader.visibility = View.VISIBLE
            }

        }

    }
    private fun fetchDataFromNetwork(){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.approvedMerchatApi()
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.pendingMerchantRv.apply {
            maerchantAdapter=MerchantPendingAdapter(requireContext(),merchantList)
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=maerchantAdapter
        }
    }
}