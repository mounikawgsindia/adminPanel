package com.wingspan.adminpanel.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.CategoriesAdapter
import com.wingspan.adminpanel.adapter.SubCategoriesAdapter
import com.wingspan.adminpanel.databinding.ActivitySubCategoryBinding
import com.wingspan.adminpanel.databinding.CategoryUpdateLayoutBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel
import com.wingspan.adminpanel.model.SubCategoriesModel
import com.wingspan.adminpanel.utils.UserPreferences
import com.wingspan.adminpanel.viewmodel.CategoriesViewModel
import com.wingspan.adminpanel.viewmodel.SubCategoriesViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class SubCategoryActivity : AppCompatActivity() {
    var _binding: ActivitySubCategoryBinding?=null
    val binding get()=_binding
    lateinit var _bindingAlertDialog: CategoryUpdateLayoutBinding
    val bindingAlertDialog get()=_bindingAlertDialog
    val viewModel: SubCategoriesViewModel by viewModels()
    private var categoriesList=ArrayList<SubCategoriesModel>()
    private var categoriesFiltredList=ArrayList<SubCategoriesModel>()
    private var isRefreshPage:Boolean=false
    private lateinit var sharedPreferences: UserPreferences
    private lateinit var categoryAdpter: SubCategoriesAdapter
    var categoryId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySubCategoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Extensions.setStatusBarColor(this@SubCategoryActivity,R.color.white)
        sharedPreferences= UserPreferences(this@SubCategoryActivity)
        setUI()
        setRecycleView()
        recycleViewSwipeDelete()
        setObservers()

        apiCall()
        //refresh
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            isRefreshPage=true
            apiCall()
        }


    }
    private fun setUI(){
        binding?.apply {
            categoryId=intent.getStringExtra("categoryid").toString()
            backIcon.setDebouncedClickListener(){
                onBackPressedDispatcher.onBackPressed()
            }
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
        viewModel.isLoading.observe(this@SubCategoryActivity){isLoading->
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
        viewModel.categoryResponse.observe(this@SubCategoryActivity){response->

            categoriesList.clear()
            binding?.swipeRefreshLayout?.isRefreshing = false
            Log.d("observer","observer main class --->$response")
            categoriesList= response as ArrayList<SubCategoriesModel>
            if(categoriesList.size==0){
                binding?.categoryRv?.visibility = View.GONE
                binding?.listEmpty?.visibility = View.VISIBLE
                binding?.listEmpty?.text="Empty List"
            }
            else{
                binding?.categoryRv?.visibility = View.VISIBLE
                binding?.listEmpty?.visibility = View.GONE
                val categoryId=intent.getStringExtra("categoryid")
                categoriesFiltredList.clear()
                categoriesList.forEach{item->
                    if(item.categorie_id==categoryId){
                        categoriesFiltredList.add(item)
                    }
                }
                categoryAdpter.setData(categoriesFiltredList)
            }

        }
        viewModel.categoryResponseError.observe(this@SubCategoryActivity){error->
            Extensions.showCustomSnackbar(this@SubCategoryActivity,error,R.color.light_red)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
        viewModel.categoryDeleteResponse.observe(this@SubCategoryActivity){response->

            Extensions.showCustomSnackbar(this@SubCategoryActivity,response.message.toString(),R.color.green)
        }
        viewModel.categoryDeleteError.observe(this@SubCategoryActivity){error->
            Extensions.showCustomSnackbar(this@SubCategoryActivity,error,R.color.light_red)

        }
        viewModel.categoryPostResponse.observe(this@SubCategoryActivity){response->
            binding?.loader?.visibility= View.GONE
            Extensions.showCustomSnackbar(this@SubCategoryActivity,response?.message.toString(),R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall()

        }
        viewModel.categoryPostResponseError.observe(this@SubCategoryActivity){error->
            binding?.loader?.visibility= View.GONE
            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(this@SubCategoryActivity,error,R.color.light_red)
        }

        viewModel.categoryUpdateResponse.observe(this@SubCategoryActivity){response->

            Extensions.showCustomSnackbar(this@SubCategoryActivity,response?.message.toString(),R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall()

        }
        viewModel.categoryUpdateResponseError.observe(this@SubCategoryActivity){error->

            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(this@SubCategoryActivity,error,R.color.light_red)
        }
    }
    private fun setRecycleView(){
        binding?.categoryRv?.apply {
            categoryAdpter= SubCategoriesAdapter(this@SubCategoryActivity,categoriesList,viewModel)
            layoutManager=
                LinearLayoutManager(this@SubCategoryActivity, LinearLayoutManager.VERTICAL,false)
            adapter=categoryAdpter
        }
    }
    private fun apiCall(){
        if(Extensions.isNetworkAvailable(this@SubCategoryActivity)){
            viewModel.getSubCategories(intent.getStringExtra("categoryid").toString())
        }else{
            Extensions.showNetworkAlertDialog(this@SubCategoryActivity)
        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun categoryAlertDialog(){
        val dialog= BottomSheetDialog(this@SubCategoryActivity)
        _bindingAlertDialog= CategoryUpdateLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bindingAlertDialog.root)
        bindingAlertDialog.apply {
            upload.setDebouncedClickListener {
                heading.text="Add Categoty"

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

                    viewModel.uploadSubCategory(categoryId,nameEt.text.toString())
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
        val swipeCallBack=SwipeCallback(this@SubCategoryActivity,categoryAdpter)
        val itemTouchHelper= ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding?.categoryRv)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    inner class SwipeCallback(context: Context, private val adapter: SubCategoriesAdapter):
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

                                viewModel.deleteRecordApi(deletedItem.item_id.toString())

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