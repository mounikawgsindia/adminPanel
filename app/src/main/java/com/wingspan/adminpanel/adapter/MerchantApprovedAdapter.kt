package com.wingspan.adminpanel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wingspan.adminpanel.databinding.CustomMernantPendingListBinding
import com.wingspan.adminpanel.fragments.MerchantApprovedFragment
import com.wingspan.adminpanel.fragments.MerchantNonApprovals
import com.wingspan.adminpanel.fragments.MerchatPendingFragment
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.PendingMerchants

class MerchantApprovedAdapter (val context: Context, var list:ArrayList<ApprovedMerchants>):
    RecyclerView.Adapter<MerchantApprovedAdapter.ViewHolder> (){
    class ViewHolder(var binding: CustomMernantPendingListBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var root=
            CustomMernantPendingListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listData=list[position]
        holder.binding.apply {

        }
    }
    fun setData(newData:ArrayList<ApprovedMerchants>){
        list=newData
        notifyDataSetChanged()
    }
}