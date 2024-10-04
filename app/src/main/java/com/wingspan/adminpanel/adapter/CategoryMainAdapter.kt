package com.wingspan.adminpanel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wingspan.adminpanel.databinding.CustomCategoryMainLayoutBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.CategoriesModel


class CategoryMainAdapter(val context: Context,
                          private val categories: ArrayList<CategoriesModel>,
                          private val onCategoryClick: (String, Int) -> Unit
) : RecyclerView.Adapter<CategoryMainAdapter.ViewHolder>() {


    class ViewHolder(var binding: CustomCategoryMainLayoutBinding) :RecyclerView.ViewHolder(binding.root){

    }
    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root=CustomCategoryMainLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
       return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val category = categories[position]
        holder.binding.itemTitle.text = category.describtion
        val bitmap = Extensions.decodeSampledBitmapFromResource(context.resources, category.image!!, 200, 200)

    holder.binding.categoryCI.setImageBitmap(bitmap)


        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onCategoryClick(categories[position].describtion!!,position)
        }
    }
    fun getSelectedPosition(): Int = selectedPosition
}

