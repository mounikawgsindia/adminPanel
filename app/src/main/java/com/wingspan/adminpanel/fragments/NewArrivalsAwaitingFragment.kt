package com.wingspan.adminpanel.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.FlashSaleAwaitingAdapter
import com.wingspan.adminpanel.adapter.NewArrivalsAwaitingAdapter
import com.wingspan.adminpanel.databinding.FragmentFlashSaleAwaitingBinding
import com.wingspan.adminpanel.databinding.FragmentNewArrivalsAwaitingBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.AwaitingNewArrivals
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel
import com.wingspan.adminpanel.viewmodel.NewArrivalsViewModel


class NewArrivalsAwaitingFragment : Fragment() {

    lateinit var _binding: FragmentNewArrivalsAwaitingBinding
    val binding get()=_binding
    lateinit var alertDialog: AlertDialog
    lateinit var newArrivalsAdapter: NewArrivalsAwaitingAdapter
    var newArrivalsList=ArrayList<AwaitingNewArrivals>()
    private val viewModel: NewArrivalsViewModel by viewModels()
    lateinit var viewPager:ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentNewArrivalsAwaitingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromNetwork(false)
        Log.d("oncreate","oncreateview")
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
            newArrivalAwaitingRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !newArrivalAwaitingRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
            // Detect when scrolling vertically and disable swipe gestures
            newArrivalAwaitingRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

            approveAll.setDebouncedClickListener(){
                showAlertDialog()
            }
        }
    }
    private fun setObserver(){

        viewModel.newArrivalsPendingError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsPendingResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            newArrivalsList.clear()
            Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            newArrivalsList= response as ArrayList<AwaitingNewArrivals>
            if(newArrivalsList.size==0){
                binding.newArrivalAwaitingRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
                binding.approveAll.visibility=View.GONE
                val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tab_layout_flashsale)
                val rejectTab = tabLayout.getTabAt(0)
                val badge = rejectTab?.orCreateBadge
                badge?.isVisible = false
            }
            else{
                binding.newArrivalAwaitingRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                newArrivalsAdapter.setData(newArrivalsList)
                binding.approveAll.visibility=View.VISIBLE
                // Assuming "Reject" is the second tab
                val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tab_layout_flashsale)
                val rejectTab = tabLayout.getTabAt(0)
                val badge = rejectTab?.orCreateBadge
                badge?.number = newArrivalsList.size // Example badge count
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
        viewModel.newArrivalsRejectAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.newArrivalsRejectAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsApprovedAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.newArrivalsApprovedAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsApprovedAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsApprovedAllError.observe(viewLifecycleOwner){error->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.newArrivalsApprovedAllResponse.observe(viewLifecycleOwner){response->
            Extensions.showCustomSnackbar(requireContext(),response?.message!!, R.color.green)
            fetchDataFromNetwork(false)
        }


    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.awaitingNewArrivalApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.newArrivalAwaitingRv.apply {
            newArrivalsAdapter= NewArrivalsAwaitingAdapter(viewModel,requireContext(),newArrivalsList)
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=newArrivalsAdapter
        }
    }
    private fun showAlertDialog(){
        val builder= AlertDialog.Builder(requireContext())
        builder.setTitle("Approve All")
        builder.setMessage("Are you sure want to Appro all New Arrivals?")
        builder.setPositiveButton("OK"){dialog,which->
            viewModel.approveallApi()
        }
        builder.setNegativeButton("CANCEL"){dialog,which->
            dialog.dismiss()
        }
        alertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
    override fun onDestroyView() {
        super.onDestroyView()

    }


}