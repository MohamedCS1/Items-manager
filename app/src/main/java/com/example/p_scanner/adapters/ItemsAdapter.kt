package com.example.p_scanner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.R
import com.example.p_scanner.interfaces.ItemClickListener

class ProductsAdapter:RecyclerView.Adapter<ProductViewHolder>() {

    var arrayOfItems = arrayListOf<Item>()
    lateinit var itemClickListener:ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item ,parent ,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = arrayOfItems[position].name
        holder.description.text = arrayOfItems[position].description
        holder.price.text = arrayOfItems[position].price+" DA"
        holder.buMenu.setOnClickListener {
            itemClickListener.onClick(arrayOfItems[position])
        }
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

    fun onItemClick(listener: ItemClickListener)
    {
        this.itemClickListener = listener
    }
}

class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    val title = itemView.findViewById<TextView>(R.id.title)
    val description = itemView.findViewById<TextView>(R.id.description)
    val price = itemView.findViewById<TextView>(R.id.price)
    val buMenu = itemView.findViewById<ImageView>(R.id.buMenu)
}
