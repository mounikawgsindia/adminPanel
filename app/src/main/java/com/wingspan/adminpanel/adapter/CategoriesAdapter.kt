package com.wingspan.adminpanel.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingspan.adminpanel.databinding.CustomCategoriesBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel


class CategoriesAdapter(val context: Context, val list:ArrayList<CategoriesModel>):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewModel>() {
    class CategoriesViewModel(val binding: CustomCategoriesBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewModel {
        val root=CustomCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoriesViewModel(root)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: CategoriesViewModel, position: Int) {
        val listData=list[position]
       holder.binding.apply {
           val bitmap = Extensions.decodeSampledBitmapFromResource(context.resources, listData.image!!, 200, 200)
           categoryCI.setImageBitmap(bitmap)
           name.text=listData.describtion
           categoryCI.setDebouncedClickListener(){
//               val intent = Intent(context, CategoriesMainView::class.java)
//               context.startActivity(intent)
           }
       }
    }
}