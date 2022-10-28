package com.example.p_scanner.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Pojo.Product
import com.example.p_scanner.R

class ProductsAdapter:RecyclerView.Adapter<ProductViewHolder>() {

    var arrayOfProducts = arrayListOf<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_product ,parent ,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = arrayOfProducts[position].name
        holder.description.text = arrayOfProducts[position].description
        holder.price.text = arrayOfProducts[position].price+" DA"
    }

    override fun getItemCount(): Int {
        return arrayOfProducts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(arrayOfProduct:ArrayList<Product>)
    {
        this.arrayOfProducts = arrayOfProduct
        notifyDataSetChanged()
    }
}

class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    val title = itemView.findViewById<TextView>(R.id.title)
    val description = itemView.findViewById<TextView>(R.id.description)
    val price = itemView.findViewById<TextView>(R.id.price)
}
