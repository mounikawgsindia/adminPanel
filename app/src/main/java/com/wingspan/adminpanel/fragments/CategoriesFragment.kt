package com.wingspan.adminpanel.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.CategoriesAdapter
import com.wingspan.adminpanel.databinding.CategoryUpdateLayoutBinding
import com.wingspan.adminpanel.databinding.FragmentCategories1Binding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel
import com.wingspan.adminpanel.utils.UserPreferences
import com.wingspan.adminpanel.viewmodel.CategoriesViewModel


class CategoriesFragment : Fragment() {

    var _binding: FragmentCategories1Binding?=null
    val binding get()=_binding
    lateinit var _bindingAlertDialog: CategoryUpdateLayoutBinding
    val bindingAlertDialog get()=_bindingAlertDialog
    val viewModel: CategoriesViewModel by viewModels()
    private var categoriesList=ArrayList<CategoriesModel>()
    private var isRefreshPage:Boolean=false

    private lateinit var sharedPreferences: UserPreferences
    private lateinit var categoryAdpter: CategoriesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentCategories1Binding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences= UserPreferences(requireActivity())
        setUI()
        setRecycleView()
        recycleViewSwipeDelete()
        setObservers()
        apiCall(false)
        //refresh
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            isRefreshPage=true
            apiCall(isRefreshPage)
        }
    }
    private fun setUI(){
        binding?.apply {

            add.setDebouncedClickListener {
                categoryAlertDialog()
            }
            categoryRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !categoryRv.canScrollVertically(-1)
                    swipeRefreshLayout.isEnabled = isAtTop
                }
            })


        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun setObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            if(isLoading){
                binding?.shimmerLayout?.visibility= View.VISIBLE
                binding?.categoryRv?.visibility = View.GONE
                binding?.shimmerLayout?.startShimmer()
            }else{
                binding?.shimmerLayout?.stopShimmer()
                binding?.shimmerLayout?.visibility = View.GONE
                binding?.categoryRv?.visibility = View.VISIBLE
            }

        }
        viewModel.categoryResponse.observe(viewLifecycleOwner){response->

            categoriesList.clear()
            binding?.swipeRefreshLayout?.isRefreshing = false
            Log.d("observer","observer main class --->$response")
            categoriesList= response as ArrayList<CategoriesModel>
            if(categoriesList.size==0){
                binding?.categoryRv?.visibility = View.GONE
                binding?.listEmpty?.visibility = View.VISIBLE
                binding?.listEmpty?.text="Empty List"
            }
            else{
                binding?.categoryRv?.visibility = View.VISIBLE
                binding?.listEmpty?.visibility = View.GONE
                categoryAdpter.setData(categoriesList)
            }

        }
        viewModel.categoryResponseError.observe(viewLifecycleOwner){error->
            Extensions.showCustomSnackbar(requireActivity(),error, R.color.light_red)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
        viewModel.categoryDeleteResponse.observe(viewLifecycleOwner){response->

            Extensions.showCustomSnackbar(requireActivity(),response.message.toString(), R.color.green)
        }
        viewModel.categoryDeleteError.observe(viewLifecycleOwner){error->
            Extensions.showCustomSnackbar(requireActivity(),error, R.color.light_red)
        }
        viewModel.categoryPostResponse.observe(viewLifecycleOwner){response->
            binding?.loader?.visibility= View.GONE
            Extensions.showCustomSnackbar(requireActivity(),response?.message.toString(), R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall(true)
        }
        viewModel.categoryPostResponseError.observe(viewLifecycleOwner){error->
            binding?.loader?.visibility= View.GONE
            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }

        viewModel.categoryUpdateResponse.observe(viewLifecycleOwner){response->

            Extensions.showCustomSnackbar(requireContext(),response?.message.toString(), R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall(true)

        }
        viewModel.categoryUpdateResponseError.observe(viewLifecycleOwner){error->

            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(requireContext(),error, R.color.light_red)
        }
    }
    private fun setRecycleView(){
        binding?.categoryRv?.apply {
            categoryAdpter= CategoriesAdapter(requireContext(),categoriesList,viewModel)
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=categoryAdpter
        }
    }
    private fun apiCall(isRefreshPage:Boolean){
        if(Extensions.isNetworkAvailable(requireContext())){
            viewModel.getCategories(isRefreshPage)
        }else{
            Extensions.showNetworkAlertDialog(requireContext())
        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun categoryAlertDialog(){
        val dialog= BottomSheetDialog(requireContext())
        _bindingAlertDialog=CategoryUpdateLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bindingAlertDialog.root)
        bindingAlertDialog.apply {
            upload.setDebouncedClickListener {


                if(nameEt.text.toString().isEmpty()){
                    nameErrorTV.visibility= View.VISIBLE
                    nameErrorTV.text="Category Name Required"
                }else{
                    nameErrorTV.visibility= View.GONE
                }

                if(nameEt.text.toString().isNotEmpty())
                {
                    dialog.dismiss()
                    binding?.loader?.visibility= View.VISIBLE
                    viewModel.uploadCategory(nameEt.text.toString())
                }

            }
            backIv.setDebouncedClickListener {
                dialog.dismiss()
            }

        }

        dialog.show()
    }




    private fun recycleViewSwipeDelete(){
        //swipe Delete
        val swipeCallBack=SwipeCallback(requireContext(),categoryAdpter)
        val itemTouchHelper= ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding?.categoryRv)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    inner class SwipeCallback(context: Context, private val adapter:CategoriesAdapter):
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete)
        private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
        private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
        private val background = ColorDrawable()
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.3f
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    // Handle delete action
                    val deletedItem = categoriesList[position]
                    adapter.deleteItem(position)
                    val snackbar =
                        Snackbar.make(binding!!.categoryRv, "Item deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                adapter.addItem(position, deletedItem)
                            }
                    snackbar.show()
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            if (event != DISMISS_EVENT_ACTION) {

                                viewModel.deleteRecordApi(deletedItem.categorie_id.toString())

                            }
                        }
                    })
                }
            }

        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top

            if (dX < 0) { // Swiping to the left
                // Draw background
                background.color = Color.parseColor("#D32F2F")
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                // Calculate icon position
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = 20
                val deleteIconLeft = itemView.right - intrinsicWidth - deleteIconMargin
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Draw the delete icon
                deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon?.draw(c)
            } else {
                // No swipe or swiping to the right
                background.setBounds(0, 0, 0, 0)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }


}