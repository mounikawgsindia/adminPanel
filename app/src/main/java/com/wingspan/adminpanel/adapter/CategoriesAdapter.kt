package com.wingspan.adminpanel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wingspan.adminpanel.databinding.CategoryUpdateLayoutBinding
import com.wingspan.adminpanel.databinding.CustomCategoriesListBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel


class CategoriesAdapter(
    val context: Context, var list:ArrayList<CategoriesModel>,
    var viewModel: com.wingspan.adminpanel.viewmodel.CategoriesViewModel
):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewModel>() {
    lateinit var _bindingAlertDialog:CategoryUpdateLayoutBinding
    val bindingAlertDialog get()=_bindingAlertDialog
    class CategoriesViewModel(val binding: CustomCategoriesListBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewModel {
        val root= CustomCategoriesListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoriesViewModel(root)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: CategoriesViewModel, position: Int) {
        val listData=list[position]
       holder.binding.apply {
         //  val bitmap = Extensions.decodeSampledBitmapFromResource(context.resources, listData.image!!, 200, 200)
          // categoryCI.setImageBitmap(bitmap)
           name.text=listData.categorie_name
           update.setDebouncedClickListener(){
               categoryAlertDialog(listData.categorie_id.toString(),listData.categorie_name.toString())
           }
       }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData:ArrayList<CategoriesModel>){
        list=newData
        notifyDataSetChanged()
    }
    fun deleteItem(position:Int){
        if(position>=0 && position<list.size){
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,list.size)
        }else{
            Log.e("Error", "Invalid position for deletion")
        }

    }
    fun addItem(position: Int,newItem: CategoriesModel){
        list.add(position,newItem)
        notifyItemInserted(position)

    }
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun categoryAlertDialog(id:String,name:String){
        val dialog= BottomSheetDialog(context)
        _bindingAlertDialog= CategoryUpdateLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingAlertDialog.root)



        bindingAlertDialog.apply {
            nameEt.setText(name)

            upload.setDebouncedClickListener {
                if(nameEt.text.toString().isEmpty()){
                    nameErrorTV.visibility= View.VISIBLE
                    nameErrorTV.text="Title Required"
                }else{
                    nameErrorTV.visibility= View.GONE
                }

                if(nameEt.text.toString().isNotEmpty())
                {
                    dialog.dismiss()
                    viewModel.editCategory(id,name)
                }

            }
            backIv.setDebouncedClickListener {
                dialog.dismiss()
            }

        }

        dialog.show()
    }
}