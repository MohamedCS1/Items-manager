package com.example.p_scanner.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.R

class ProductsAdapter:RecyclerView.Adapter<ProductViewHolder>() {

    var arrayOfItems = arrayListOf<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item ,parent ,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = arrayOfItems[position].name
        holder.description.text = arrayOfItems[position].description
        holder.price.text = arrayOfItems[position].price+" DA"
    }

    override fun getItemCount(): Int {
        return arrayOfItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(arrayOfItem:ArrayList<Item>)
    {
        this.arrayOfItems = arrayOfItem
        notifyDataSetChanged()
    }
}

class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    val title = itemView.findViewById<TextView>(R.id.title)
    val description = itemView.findViewById<TextView>(R.id.description)
    val price = itemView.findViewById<TextView>(R.id.price)
}
