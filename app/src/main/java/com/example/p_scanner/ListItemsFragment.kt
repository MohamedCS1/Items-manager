package com.example.p_scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Adapters.ProductsAdapter
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.ViewModels.ProductViewModel

class ListItemsFragment : Fragment() {
    lateinit var adapter: ProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
    lateinit var productViewModel:ProductViewModel
        super.onCreate(savedInstanceState)

        productViewModel = ProductViewModel(this ,requireContext())

        adapter = ProductsAdapter()

        productViewModel.getListItemLiveData.observe(this,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                adapter.setList(listItems!! as ArrayList<Item>)
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