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
import com.google.android.material.tabs.TabLayout
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.FlashSaleApprovedAdapter
import com.wingspan.adminpanel.adapter.FlashSaleAwaitingAdapter
import com.wingspan.adminpanel.databinding.FragmentFlashSaleAwaitingBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.ApprovedFlashSale
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel


class FlashSaleAwaitingFragment : Fragment() {
    lateinit var _binding: FragmentFlashSaleAwaitingBinding
    val binding get()=_binding
    lateinit var flashSaleAdapter: FlashSaleAwaitingAdapter
    var flashSaleList=ArrayList<AwaitingFlashSale>()
    private val viewModel: FlashSaleViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFlashSaleAwaitingBinding.inflate(layoutInflater)
        return binding.root
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
            flashsaleAwaitingRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !flashsaleAwaitingRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
        }
    }
    private fun setObserver(){

        viewModel.flashSalePendingError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.flashSalePendingResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            flashSaleList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            flashSaleList= response as ArrayList<AwaitingFlashSale>
            if(flashSaleList.size==0){
                binding.flashsaleAwaitingRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"

            }
            else{
                binding.flashsaleAwaitingRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                flashSaleAdapter.setData(flashSaleList)
                val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tab_layout_flashsale)
                val rejectTab = tabLayout.getTabAt(0) // Assuming "Reject" is the second tab
                val badge = rejectTab?.orCreateBadge
                badge?.number = flashSaleList.size // Example badge count
                badge?.isVisible = true // Make the badge visible
            }
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if(isLoading){

                binding.loader.visibility = View.VISIBLE

            }else{
                binding.loader.visibility = View.GONE
            }

        }
        viewModel.flashSaleRejectAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.flashSaleRejectAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }

    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.awaitingFlashSaleApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.flashsaleAwaitingRv.apply {
            flashSaleAdapter= FlashSaleAwaitingAdapter(viewModel,requireContext(),flashSaleList)
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=flashSaleAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }

}