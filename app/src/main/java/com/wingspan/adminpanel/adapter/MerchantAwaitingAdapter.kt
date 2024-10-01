package com.wingspan.adminpanel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingspan.adminpanel.databinding.CustomMernantPendingListBinding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.viewmodel.ShopKeeperViewModel

class MerchantAwaitingAdapter(val viewModel:ShopKeeperViewModel,val context: Context, var list:ArrayList<AwaitingMerchants>):RecyclerView.Adapter<MerchantAwaitingAdapter.ViewHolder> (){
    class ViewHolder(var binding:CustomMernantPendingListBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var root=CustomMernantPendingListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val listData=list[position]
        holder.binding.apply {
            shopName.text=listData.shopname
            address.text=listData.shopaddress
            name.text=listData.fullname
            gstNumber.text=listData.gstnumber
            mobileNumber.text=listData.phnumber
            email.text=listData.email
            approved.setDebouncedClickListener(){
                viewModel.acceptShopkeeperApi(listData.id.toString(),context)
            }
            rejected.setDebouncedClickListener {
                viewModel.rejectShopkeeperApi(listData.id.toString(),context)
            }
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData:ArrayList<AwaitingMerchants>){
        list=newData
        notifyDataSetChanged()
    }
}