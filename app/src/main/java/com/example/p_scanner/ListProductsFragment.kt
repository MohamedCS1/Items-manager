package com.example.p_scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Adapters.ProductsAdapter
import com.example.p_scanner.DAO.ProductDAO
import com.example.p_scanner.Database.ProductsDatabase
import com.example.p_scanner.Pojo.Product
import com.example.p_scanner.ViewModels.ProductViewModel

class ListProductsFragment : Fragment() {
    lateinit var adapter: ProductsAdapter
    lateinit var productViewModel:ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel = ProductViewModel(this ,requireContext())

        adapter = ProductsAdapter()

        productViewModel.getListProductLiveData.observe(this,object :Observer<List<Product>>{
            override fun onChanged(ListProducts: List<Product>?) {
                adapter.setList(ListProducts!! as ArrayList<Product>)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_products, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_products)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        return view
    }

}