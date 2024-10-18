package com.wingspan.adminpanel.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.CategoriesAdapter
import com.wingspan.adminpanel.databinding.ActivityCategoriesBinding
import com.wingspan.adminpanel.databinding.OffersUploadLayoutBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel
import com.wingspan.adminpanel.utils.UserPreferences
import com.wingspan.adminpanel.viewmodel.CategoriesViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class CategoriesActivity : AppCompatActivity() {
    var _binding: ActivityCategoriesBinding?=null
    val binding get()=_binding
    lateinit var _bindingAlertDialog: OffersUploadLayoutBinding
    val bindingAlertDialog get()=_bindingAlertDialog
    val viewModel:CategoriesViewModel by viewModels()
    private var categoriesList=ArrayList<CategoriesModel>()
    private var isRefreshPage:Boolean=false
    private val REQUEST_PERMISSION_CODE = 1
    var photoURI: Uri?=null
    // image pathes
    private var flashSaleImageUrl: Uri? = null
    private var flashSaleName: String? = null
    private lateinit var currentPhotoPath: String
    private lateinit var sharedPreferences: UserPreferences
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var categoryAdpter: CategoriesAdapter

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Extensions.setStatusBarColor(this@CategoriesActivity,R.color.white)

        //checkAndRequestPermissions()
        sharedPreferences= UserPreferences(this@CategoriesActivity)
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
//        // Initialize takePictureLauncher
//        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
//            if (success && photoURI != null) {
//                handleImageResult(photoURI)
//            }
//        }
//
//        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//        {
//                result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                data?.data?.let { uri ->
//                    Log.d("TAG", "Selected Image URI: $uri")
//                    handleImageResult(uri) // Call your method to handle the image URI
//                }
//            }
//        }

    }
    private fun setUI(){
        binding?.apply {
            backIcon.setDebouncedClickListener(){
                onBackPressedDispatcher.onBackPressed()
            }
            add.setDebouncedClickListener {
                categoryAlertDialog()
            }
            flashSaleRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Enable swipe-to-refresh only if the RecyclerView is scrolled to the top
                    val isAtTop = !flashSaleRv.canScrollVertically(-1)
                    swipeRefreshLayout.isEnabled = isAtTop
                }
            })


        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun setObservers(){
        viewModel.isLoading.observe(this@CategoriesActivity){isLoading->
            if(isLoading){
                binding?.shimmerLayout?.visibility= View.VISIBLE
                binding?.flashSaleRv?.visibility = View.GONE
                binding?.shimmerLayout?.startShimmer()
            }else{
                binding?.shimmerLayout?.stopShimmer()
                binding?.shimmerLayout?.visibility = View.GONE
                binding?.flashSaleRv?.visibility = View.VISIBLE
            }

        }
        viewModel.categoryResponse.observe(this@CategoriesActivity){response->

            categoriesList.clear()
            binding?.swipeRefreshLayout?.isRefreshing = false
            Log.d("observer","observer main class --->$response")
            categoriesList= response as ArrayList<CategoriesModel>
            if(categoriesList.size==0){
                binding?.flashSaleRv?.visibility = View.GONE
                binding?.listEmpty?.visibility = View.VISIBLE
                binding?.listEmpty?.text="Empty List"
            }
            else{
                binding?.flashSaleRv?.visibility = View.VISIBLE
                binding?.listEmpty?.visibility = View.GONE
                categoryAdpter.setData(categoriesList)
            }

        }
        viewModel.categoryResponseError.observe(this@CategoriesActivity){error->
            Extensions.showCustomSnackbar(this@CategoriesActivity,error,R.color.light_red)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
        viewModel.categoryDeleteResponse.observe(this@CategoriesActivity){response->

            Extensions.showCustomSnackbar(this@CategoriesActivity,response.message.toString(),R.color.green)
        }
        viewModel.categoryDeleteError.observe(this@CategoriesActivity){error->
            Extensions.showCustomSnackbar(this@CategoriesActivity,error,R.color.light_red)

        }
        viewModel.categoryPostResponse.observe(this@CategoriesActivity){response->
            binding?.loader?.visibility= View.GONE
            Extensions.showCustomSnackbar(this@CategoriesActivity,response?.message.toString(),R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall(true)

        }
        viewModel.categoryPostResponseError.observe(this@CategoriesActivity){error->
            binding?.loader?.visibility= View.GONE
            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(this@CategoriesActivity,error,R.color.light_red)
        }

        viewModel.categoryUpdateResponse.observe(this@CategoriesActivity){response->

            Extensions.showCustomSnackbar(this@CategoriesActivity,response?.message.toString(),R.color.green)
            Log.d("observer","observer flahSalePostResponse --->$response")
            apiCall(true)

        }
        viewModel.categoryUpdateResponseError.observe(this@CategoriesActivity){error->

            Log.d("observer","observer flahSalePostResponse error --->$error")
            Extensions.showCustomSnackbar(this@CategoriesActivity,error,R.color.light_red)
        }
    }
    private fun setRecycleView(){
        binding?.flashSaleRv?.apply {
            categoryAdpter= CategoriesAdapter(this@CategoriesActivity,categoriesList,viewModel)
            layoutManager=
                LinearLayoutManager(this@CategoriesActivity, LinearLayoutManager.VERTICAL,false)
            adapter=categoryAdpter
        }
    }
    private fun apiCall(isRefreshPage:Boolean){
        if(Extensions.isNetworkAvailable(this@CategoriesActivity)){
            viewModel.getCategories(isRefreshPage)
        }else{
            Extensions.showNetworkAlertDialog(this@CategoriesActivity)
        }
    }
//    //camera pics
//    private fun handleImageResult(imageUri: Uri?) {
//        imageUri?.let { uri ->
//            bindingAlertDialog.image.setImageURI(uri)
//            flashSaleImageUrl = uri
//            flashSaleName = getFileName(uri)
//            Log.d("TAG","---> handleImageResult ${flashSaleImageUrl}... ${flashSaleName}")
//        }
//    }
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun categoryAlertDialog(){
        val dialog= BottomSheetDialog(this@CategoriesActivity)
        _bindingAlertDialog=OffersUploadLayoutBinding.inflate(layoutInflater)
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
//            gallery.setDebouncedClickListener {
//                openGallery()
//            }
//            camera.setDebouncedClickListener(){
//                cameraPicCaptureIntent()
//            }
        }

        dialog.show()
    }
    //gallery image
    private fun openGallery() {
        Log.d("TAG","---> openGallery")

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getImageLauncher.launch(intent)
    }
    //camera pics
    @SuppressLint("QueryPermissionsNeeded")
    private fun cameraPicCaptureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
        photoFile?.let {
            photoURI = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                it
            )
            photoURI?.let { uri ->
                takePictureLauncher.launch(uri) // Launch camera intent
            }
        }
    }
    //camera capture image file creation for db storage
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(java.util.Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    private fun checkAndRequestPermissions(): Boolean {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

        } else {
            // For devices below Android 13, you still need to request READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQUEST_PERMISSION_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            val perms = HashMap<String, Int>()
            perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                perms[Manifest.permission.READ_MEDIA_IMAGES] = PackageManager.PERMISSION_GRANTED

            } else {
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
            }

            for (i in permissions.indices) {
                perms[permissions[i]] = grantResults[i]
            }

            if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED &&
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                        perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        perms[Manifest.permission.READ_MEDIA_IMAGES] == PackageManager.PERMISSION_GRANTED)) {
                // All permissions are granted
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                // Some permissions are not granted
                Toast.makeText(this, "Some permissions are denied. Please change your App settings", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }
    private fun recycleViewSwipeDelete(){
        //swipe Delete
        val swipeCallBack=SwipeCallback(this@CategoriesActivity,categoryAdpter)
        val itemTouchHelper= ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding?.flashSaleRv)
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
                        Snackbar.make(binding!!.flashSaleRv, "Item deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                adapter.addItem(position, deletedItem)
                            }
                    snackbar.show()
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            if (event != DISMISS_EVENT_ACTION) {
                                //clear Flash sale data in cache

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

            when {
                dX < 0 -> { // Swiping to the left
                    background.color = Color.parseColor("#D32F2F")
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    background.draw(c)

                    deleteIcon?.setBounds(
                        itemView.right - intrinsicWidth - 20,
                        itemView.top + (itemHeight - intrinsicHeight) / 2,
                        itemView.right - 20,
                        itemView.top + (itemHeight + intrinsicHeight) / 2
                    )
                    deleteIcon?.draw(c)
                }

                else -> {
                    background.setBounds(0, 0, 0, 0)
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }




}