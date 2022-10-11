package com.example.p_scanner.Adapters

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.Pojo.Product
import com.example.p_scanner.R

class ProductsAdapter:RecyclerView.Adapter<ProductViewHolder>() {

    val arrayOfProducts = arrayListOf<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LinearLayout.inflate(parent.context ,R.layout.card_product ,parent))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return arrayOfProducts.size
    }
}

class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{}