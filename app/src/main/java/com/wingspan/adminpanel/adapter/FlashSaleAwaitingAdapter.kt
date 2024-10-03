package com.wingspan.adminpanel.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.databinding.CustomFlashsaleBinding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel

class FlashSaleAwaitingAdapter (val viewModel: FlashSaleViewModel, val context: Context, var list:ArrayList<AwaitingFlashSale>):
    RecyclerView.Adapter<FlashSaleAwaitingAdapter.ViewHolder> (){

    class ViewHolder(var binding: CustomFlashsaleBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root=
            CustomFlashsaleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listData=list[position]
        holder.binding.apply {
            title.text=listData.title
            description.text=listData.description
            discountText.text=listData.description
            discountCost.text=listData.dprice
            mainCost.text=listData.price
            // endTime.text=listData.endTime
            approved.visibility= View.GONE
            mainCost.paintFlags = mainCost.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            Glide.with(context)
                .load(listData.image).override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.defaultfood_image).error(R.drawable.defaultfood_image).into(image)
            approved.setDebouncedClickListener(){
                viewModel.acceptFlashSaleApi(listData.id.toString(),context)
            }
            rejected.setDebouncedClickListener {
                viewModel.rejectFlashSaleApi(listData.id.toString(),context)
            }
        }
    }
    fun setData(newData:ArrayList<AwaitingFlashSale>){
        list=newData
        notifyDataSetChanged()
    }
}