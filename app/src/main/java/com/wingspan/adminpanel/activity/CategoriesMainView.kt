package com.wingspan.adminpanel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.adapter.CaregotyItemAdapter
import com.wingspan.adminpanel.adapter.CategoryMainAdapter
import com.wingspan.adminpanel.databinding.ActivityCategoriesMainViewBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.model.CategoriesModel



class CategoriesMainView : AppCompatActivity() {
    lateinit var _binding: ActivityCategoriesMainViewBinding
    val binding get()=_binding

    private lateinit var categoryAdapter: CategoryMainAdapter
    private lateinit var itemAdapter: CaregotyItemAdapter
    private var caretoriesList=ArrayList<CategoriesModel>()
    private var defaultCategory: String = "9 minutes develory"
    private val categories = listOf(  CategoriesModel(R.drawable.express_delivery,"9 minutes develory"),
        CategoriesModel(R.drawable.cat1,"Dailry & Cheese"),
        CategoriesModel(R.drawable.cat2,"Cold pressed juice"),
        CategoriesModel(R.drawable.cat3,"Fruits and vegitables"),
        CategoriesModel(R.drawable.cat4,"Fish and Sea Food"),
        CategoriesModel(R.drawable.cat5,"Prawns"),
        CategoriesModel(R.drawable.cat6,"Poultry"),
        CategoriesModel(R.drawable.cat7,"Mutton"),
        CategoriesModel(R.drawable.cat8,"Steaks & Fillets"),
        CategoriesModel(R.drawable.cat9,"Bone less Fillets"),
        CategoriesModel(R.drawable.cat10,"Eggs"),
        CategoriesModel(R.drawable.cat11,"Batters & Porota"),
        CategoriesModel(R.drawable.cat12,"Ready To cook"),

        )
    private val itemsMap = mapOf(
        "Dailry & Cheese" to listOf("Item 1-1", "Item 1-2", "Item 1-3","Item 1-1", "Item 1-2", "Item 1-3","Item 1-1", "Item 1-2", "Item 1-3"),
        "Cold pressed juice" to listOf("Item 2-1", "Item 2-2", "Item 2-3","Item 1-1", "Item 1-2", "Item 1-3","Item 1-1", "Item 1-2", "Item 1-3"),
        "Fruits and vegitables" to listOf("Item 3-1", "Item 3-2", "Item 3-3")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityCategoriesMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Extensions.setStatusBarColor(this@CategoriesMainView, R.color.white)
        setUI()
        setRecycleView()
    }
    private fun setUI(){
        binding.apply {
            back.setDebouncedClickListener(){
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
    private fun setRecycleView(){
        binding.apply {
            leftRecyclerView.layoutManager = LinearLayoutManager(this@CategoriesMainView,LinearLayoutManager.VERTICAL,false)
            rightRecyclerView.layoutManager = LinearLayoutManager(this@CategoriesMainView,LinearLayoutManager.VERTICAL,false)



            caretoriesList.addAll(categories)

            //set default category
            if (caretoriesList.isNotEmpty()) {
                val firstCategory = caretoriesList[0].describtion
                val items = itemsMap[firstCategory] ?: emptyList()

                itemAdapter = CaregotyItemAdapter(items)
                rightRecyclerView.adapter = itemAdapter
                moveIndicatorToSelectedItem(0)
            }
            categoryAdapter = CategoryMainAdapter(this@CategoriesMainView,caretoriesList) { category,position ->


                val items = itemsMap[category] ?: emptyList()
                Log.d("category","category   $category....$position....$items")
                itemAdapter = CaregotyItemAdapter(items)
                rightRecyclerView.adapter = itemAdapter
                // Move the indicator to the selected category
                moveIndicatorToSelectedItem(position)
                Toast.makeText(this@CategoriesMainView, "$category selected", Toast.LENGTH_SHORT).show()
            }

            leftRecyclerView.adapter = categoryAdapter
            leftRecyclerView.setHasFixedSize(true)
            rightRecyclerView.setHasFixedSize(true)
            leftRecyclerView.clearOnScrollListeners()
            rightRecyclerView.clearOnScrollListeners()
        }
    }

    private fun moveIndicatorToSelectedItem(position: Int) {
        val layoutManager = binding.leftRecyclerView.layoutManager as LinearLayoutManager
        val selectedItemView = layoutManager.findViewByPosition(position)
        binding.indicatorView.visibility= View.VISIBLE
        selectedItemView?.let {
            val itemHeight = it.height
            val itemTop = it.top

            // Update the indicator height and position
            binding.indicatorView.layoutParams = binding.indicatorView.layoutParams.apply {
                height = itemHeight
            }
            binding.indicatorView.y = itemTop.toFloat()
        }
    }

}