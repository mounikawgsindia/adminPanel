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
import androidx.viewpager2.widget.ViewPager2
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.FlashSaleApprovedAdapter
import com.wingspan.adminpanel.adapter.NewArrivalsApprovedAdapter
import com.wingspan.adminpanel.databinding.FragmentFlashSaleApprovalBinding
import com.wingspan.adminpanel.databinding.FragmentNewArrivalsApprovalBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.ApprovedFlashSale
import com.wingspan.adminpanel.model.ApprovedNewArrivals
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel
import com.wingspan.adminpanel.viewmodel.NewArrivalsViewModel


class NewArrivalsApprovalFragment : Fragment() {

    lateinit var _binding: FragmentNewArrivalsApprovalBinding
    val binding get()=_binding
    lateinit var arrrovalsAdapter: NewArrivalsApprovedAdapter
    var approvalsList=ArrayList<ApprovedNewArrivals>()
    private val viewModel: NewArrivalsViewModel by viewModels()
    lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentNewArrivalsApprovalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromNetwork(false)
        viewPager= activity?.findViewById(R.id.viewpager_new_arrivals)!!
        setUI()
        setObserver()
        setRecycleView()
    }
    private fun setUI(){
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                fetchDataFromNetwork(true)
            }
            newArrivalApprovedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !newArrivalApprovedRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
            // Detect when scrolling vertically and disable swipe gestures
            newArrivalApprovedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy != 0) {
                        viewPager.isUserInputEnabled = false  // Disable swipe gestures during vertical scroll
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        viewPager.isUserInputEnabled = true  // Re-enable swipe gestures when scroll ends
                    }
                }
            })

        }
    }
    private fun setObserver(){

        viewModel.newArrivalsAprovedError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsAprovedResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            approvalsList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            approvalsList= response as ArrayList<ApprovedNewArrivals>
            if(approvalsList.size==0){
                binding.newArrivalApprovedRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
            }
            else{
                binding.newArrivalApprovedRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                arrrovalsAdapter.setData(approvalsList)
            }
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if(isLoading){

                binding.loader.visibility = View.VISIBLE

            }else{
                binding.loader.visibility = View.GONE
            }

        }
        viewModel.newArrivalsRejectAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.newArrivalsRejectAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }

    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.approvedNewArrivalsApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.newArrivalApprovedRv.apply {
            arrrovalsAdapter= NewArrivalsApprovedAdapter(viewModel,requireContext(),approvalsList)
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=arrrovalsAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }


}