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
import com.wingspan.adminpanel.adapter.MerchantAwaitingAdapter
import com.wingspan.adminpanel.databinding.FragmentMerchantAwaitingListBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel

class MerchatAwaitingFragment : Fragment() {
    lateinit var _binding: FragmentMerchantAwaitingListBinding
    val binding get()=_binding
    private lateinit var maerchantAdapter:MerchantAwaitingAdapter
    private var merchantList=ArrayList<AwaitingMerchants>()
    private val viewModel: ShopKeeperViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMerchantAwaitingListBinding.inflate(layoutInflater)
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
        binding.apply{
            swipeRefreshLayout.setOnRefreshListener {
                fetchDataFromNetwork(true)
            }
            pendingMerchantRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !pendingMerchantRv.canScrollVertically(-1)
                    binding.swipeRefreshLayout.isEnabled = isAtTop
                }
            })
        }
    }
    private fun setObserver(){
        viewModel.merchatPendingError.observe(viewLifecycleOwner){error->
            binding.swipeRefreshLayout.isRefreshing=false
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
        viewModel.merchatPendingResponse.observe(viewLifecycleOwner){response->
            binding.swipeRefreshLayout.isRefreshing=false
            merchantList.clear()
           // Extensions.showCustomSnackbar(requireContext(),"Success", R.color.green)
            Log.d("observer","observer main class --->$response")
            merchantList= response as ArrayList<AwaitingMerchants>
            if(merchantList.size==0){
                binding.pendingMerchantRv.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
                binding.listEmpty.text="Empty List"
            }
            else{
                binding.pendingMerchantRv.visibility = View.VISIBLE
                binding.listEmpty.visibility = View.GONE
                maerchantAdapter.setData(merchantList)

                try{
                    val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tab_layout)
                    val rejectTab = tabLayout.getTabAt(0) // Assuming "Reject" is the second tab
                    val badge = rejectTab?.orCreateBadge
                    badge?.number = merchantList.size // Example badge count
                    badge?.isVisible = true // Make the badge visible
                }catch (e:Exception){

                }

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
        viewModel.merchatApprovedAwaitResponse.observe(viewLifecycleOwner) { response ->
            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            fetchDataFromNetwork(false)
        }
        viewModel.merchatApprovedAwaitError.observe(viewLifecycleOwner) { error ->
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
    }
    private fun fetchDataFromNetwork(isRefresh:Boolean){

        if(Extensions.isNetworkAvailable(requireActivity())){
            viewModel.awaitingMerchatApi(isRefresh)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }
    private fun setRecycleView(){
        binding.pendingMerchantRv.apply {
            maerchantAdapter=MerchantAwaitingAdapter(viewModel,requireContext(),merchantList)
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=maerchantAdapter
        }
    }
}