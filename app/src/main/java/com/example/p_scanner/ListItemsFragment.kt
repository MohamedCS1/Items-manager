package com.example.p_scanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.adapters.ProductsAdapter
import com.example.p_scanner.interfaces.MyButtonListener
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.viewmodels.ProductViewModel

class ListItemsFragment : Fragment() {
    lateinit var adapter: ProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
    lateinit var productViewModel:ProductViewModel
        super.onCreate(savedInstanceState)

        productViewModel = ProductViewModel(this ,requireContext())

        adapter = ProductsAdapter()

        productViewModel.listItemsLiveData.observe(this,object :Observer<List<Item>>{
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

        val swiperHelper = object : SwiperHelper(requireContext(),rv ,200){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: ArrayList<MyButton>
            ) {
                buffer.add(MyButton(requireContext() ,"Delete" ,30 ,0 ,Color.YELLOW ,object :MyButtonListener{
                    override fun onClick(pos: Int) {
                        Toast.makeText(requireContext() ,"ADD POS $pos" ,Toast.LENGTH_SHORT).show()
                    }
                } ))

                buffer.add(MyButton(requireContext() ,"Delete" ,
                   30, R.drawable.icon_torch,Color.YELLOW ,object :MyButtonListener{
                    override fun onClick(pos: Int) {

                        Toast.makeText(requireContext() ,"DELETE Pos $pos" ,Toast.LENGTH_SHORT).show()
                    }
                } ))
            }
        }
        return view
    }

}