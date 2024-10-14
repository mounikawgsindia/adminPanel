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
import com.wingspan.adminpanel.adapter.FlashSaleRejectAdapter
import com.wingspan.adminpanel.adapter.NewArrivalRejectAdapter
import com.wingspan.adminpanel.databinding.FragmentFlashSaleRejectBinding
import com.wingspan.adminpanel.databinding.FragmentNewArrivalsRejectBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.model.RejectedNewArrivals
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel
import com.wingspan.adminpanel.viewmodel.NewArrivalsViewModel


class NewArrivalsRejectFragment : Fragment() {

    lateinit var _binding: FragmentNewArrivalsRejectBinding
    val binding get()=_binding
    lateinit var rejectAdapter: NewArrivalRejectAdapter
    var rejectList=ArrayList<RejectedNewArrivals>()
    private val viewModel: NewArrivalsViewModel by viewModels()
    lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentNewArrivalsRejectBinding.inflate(layoutInflater)
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
            newAarivalsRejectedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !newAarivalsRejectedRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
            // Detect when scrolling vertically and disable swipe gestures
            newAarivalsRejectedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        viewModel.newArrivalsRejectedError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsRejectedResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            rejectList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            rejectList= response as ArrayList<RejectedNewArrivals>
            if(rejectList.size==0){
                binding.newAarivalsRejectedRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
            }
            else{
                binding.newAarivalsRejectedRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                rejectAdapter.setData(rejectList)
            }
        }
        viewModel.isLoading.observe(requireActivity()){isLoading->
            if(isLoading){

                binding.loader.visibility = View.VISIBLE

            }else{
                binding.loader.visibility = View.GONE
            }

        }
        viewModel.newArrivalsApprovedAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.newArrivalsApprovedAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }

    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.rejectedNewArrivalsApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.newAarivalsRejectedRv.apply {
            rejectAdapter= NewArrivalRejectAdapter(viewModel,requireContext(),rejectList)
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=rejectAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }

}