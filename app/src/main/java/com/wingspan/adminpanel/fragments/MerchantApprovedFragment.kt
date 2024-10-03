package com.wingspan.adminpanel.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.MerchantApprovedAdapter
import com.wingspan.adminpanel.adapter.MerchantAwaitingAdapter
import com.wingspan.adminpanel.databinding.FragmentMerchantApprovalsBinding
import com.wingspan.adminpanel.databinding.FragmentMerchantBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel


class MerchantApprovedFragment : Fragment() {

    lateinit var _binding: FragmentMerchantApprovalsBinding
    val binding get()=_binding
    lateinit var maerchantAdapter: MerchantApprovedAdapter
    var merchantList=ArrayList<ApprovedMerchants>()
    private val viewModel: ShopKeeperViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantApprovalsBinding.inflate(inflater, container, false)
        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromNetwork(false)

        setUI()
        setObserver()
        setRecycleView()
    }
    private fun setUI(){
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                fetchDataFromNetwork(true)
            }
            approvedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !approvedRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
        }
    }
    private fun setObserver(){

        viewModel.merchatAprovedError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.merchatAprovedResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            merchantList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            merchantList= response as ArrayList<ApprovedMerchants>
            if(merchantList.size==0){
                binding.approvedRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
            }
            else{
                binding.approvedRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                maerchantAdapter.setData(merchantList)
            }
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if(isLoading){

                binding.loader.visibility = View.VISIBLE

            }else{
                binding.loader.visibility = View.GONE
            }

        }
        viewModel.merchatRejectAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.merchatRejectAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }

    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.approvedMerchatApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.approvedRv.apply {
            maerchantAdapter= MerchantApprovedAdapter(viewModel,requireContext(),merchantList)
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=maerchantAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }
}