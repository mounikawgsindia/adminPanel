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
import com.wingspan.adminpanel.model.RejectedNewArrivals
import com.wingspan.adminpanel.viewmodel.FlashSaleViewModel
import com.wingspan.adminpanel.viewmodel.NewArrivalsViewModel

class NewArrivalRejectAdapter(val viewModel: NewArrivalsViewModel, val context: Context, var list:ArrayList<RejectedNewArrivals>):
    RecyclerView.Adapter<NewArrivalRejectAdapter.ViewHolder> (){

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
            quantity.text=listData.quantity
            rejected.visibility= View.GONE
            mainCost.paintFlags = mainCost.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            Glide.with(context)
                .load(listData.image).override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.defaultfood_image).error(
                    R.drawable.defaultfood_image).into(image)
            approved.setDebouncedClickListener {
                viewModel.acceptNewArrivalsApi(listData.id.toString(),context)
            }
        }
    }
    fun setData(newData:ArrayList<RejectedNewArrivals>){
        list=newData
        notifyDataSetChanged()
    }
}