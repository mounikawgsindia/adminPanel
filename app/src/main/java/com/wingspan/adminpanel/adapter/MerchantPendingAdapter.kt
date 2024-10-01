package com.wingspan.adminpanel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingspan.adminpanel.databinding.CustomMernantPendingListBinding
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.PendingMerchants

class MerchantPendingAdapter(val context: Context, var list:ArrayList<PendingMerchants>):RecyclerView.Adapter<MerchantPendingAdapter.ViewHolder> (){
    class ViewHolder(var binding:CustomMernantPendingListBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var root=CustomMernantPendingListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val listData=list[position]
        holder.binding.apply {
            name.text=listData.fullname
            address.text=listData.shopaddress
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData:ArrayList<PendingMerchants>){
        list=newData
        notifyDataSetChanged()
    }
}