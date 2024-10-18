package com.wingspan.adminpanel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wingspan.adminpanel.databinding.CategoryUpdateLayoutBinding
import com.wingspan.adminpanel.databinding.CustomCategoriesListBinding
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel
import com.wingspan.adminpanel.model.SubCategoriesModel
import com.wingspan.adminpanel.viewmodel.CategoriesViewModel
import com.wingspan.adminpanel.viewmodel.SubCategoriesViewModel

class SubCategoriesAdapter(
    private val context: Context,
    private var list: ArrayList<SubCategoriesModel>,
    private val viewModel: SubCategoriesViewModel
) : RecyclerView.Adapter<SubCategoriesAdapter.CategoriesViewHolder>() {

    // Late initialization for the alert dialog binding
    private lateinit var _bindingAlertDialog: CategoryUpdateLayoutBinding
    private val bindingAlertDialog get() = _bindingAlertDialog

    // ViewHolder class
    class CategoriesViewHolder(val binding: CustomCategoriesListBinding) : RecyclerView.ViewHolder(binding.root)

    // Inflating the custom list item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CustomCategoriesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(binding)
    }

    // Returning the total item count in the list
    override fun getItemCount(): Int = list.size

    // Binding data to the view holder
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val listItem = list[position]

        holder.binding.apply {
            // Setting category name
            name.text = listItem.item_name

            // Handling the update button click
            update.setDebouncedClickListener {
                Log.d("data","data ${listItem.categorie_id.toString()}...${listItem.item_name.toString()}")
                categoryAlertDialog(listItem.categorie_id.toString(), listItem.item_name.toString(),listItem.item_id.toString())
            }
            categoryCv.setDebouncedClickListener {
                Log.d("success","success");
            }
        }
    }

    // Method to update the data in the adapter and refresh the view
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: ArrayList<SubCategoriesModel>) {
        list = newData
        notifyDataSetChanged()
    }

    // Method to delete an item from the list
    fun deleteItem(position: Int) {
        if (position in 0 until list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        } else {
            Log.e("Error", "Invalid position for deletion")
        }
    }

    // Method to add a new item to the list
    fun addItem(position: Int, newItem: SubCategoriesModel) {
        list.add(position, newItem)
        notifyItemInserted(position)
    }

    // Displaying the category update dialog
    @SuppressLint("SetTextI18n")
    private fun categoryAlertDialog(categoryId: String, name:String,itemId:String) {
        val dialog = BottomSheetDialog(context)
        _bindingAlertDialog = CategoryUpdateLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingAlertDialog.root)

        bindingAlertDialog.apply {
            // Setting the existing name in the input field
            nameEt.setText(name)

            // Handling the upload button click
            upload.setDebouncedClickListener {
                if (nameEt.text.toString().isEmpty()) {
                    nameErrorTV.visibility = View.VISIBLE
                    nameErrorTV.text = "Name Required"
                } else {
                    nameErrorTV.visibility = View.GONE
                }

                // If the input is valid, update the category
                if (nameEt.text.toString().isNotEmpty()) {
                    dialog.dismiss()
                    viewModel.editSubCategory(categoryId, nameEt.text.toString(),itemId)
                }
            }

            // Handling the back button click
            backIv.setDebouncedClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}