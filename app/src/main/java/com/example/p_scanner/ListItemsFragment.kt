package com.example.p_scanner

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.adapters.ProductsAdapter
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.interfaces.ItemClickListener
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.repository.Repository
import com.example.p_scanner.viewmodels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListItemsFragment : Fragment() {
    lateinit var adapter: ProductsAdapter
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
    lateinit var productViewModel:ProductViewModel
        super.onCreate(savedInstanceState)
        productViewModel = ProductViewModel(requireContext())

        adapter = ProductsAdapter()
        repository = Repository(ItemsDatabase.getDatabase(requireContext()).itemDAO())

        productViewModel.listItemsLiveData.observe(this,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                adapter.setList(listItems!! as ArrayList<Item>)
            }
        })

        adapter.onItemClick(object :ItemClickListener{
            override fun onClick(item: Item) {
                val bottomSheetDialog = BottomSheetDialog(requireContext() , R.style.TransparentBackgroundDialog)
                bottomSheetDialog.setContentView(R.layout.bottom_dialog)
                bottomSheetDialog.show()
                val buEdit = bottomSheetDialog.findViewById<LinearLayout>(R.id.bu_edit)
                val buDelete = bottomSheetDialog.findViewById<LinearLayout>(R.id.bu_remove)
                buEdit?.setOnClickListener {
                    val intent = Intent(requireContext() ,AddAndEditItemActivity::class.java)
                    intent.putExtra("Item" ,item)
                    intent.putExtra("Interaction" ,ItemInteractions.EDIT)
                    startActivity(intent)
                    bottomSheetDialog.hide()
                }
                buDelete?.setOnClickListener {
                    repository.deleteItemById(item.id)
                    bottomSheetDialog.hide()
                }
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_items, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_products)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        return view
    }

}