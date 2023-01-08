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
import java.util.*
import kotlin.collections.ArrayList

class ItemsAdapter:RecyclerView.Adapter<ItemViewHolder>() {

    var arrayOfItems = arrayListOf<Item>()
    lateinit var itemClickListener:ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item ,parent ,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.title.text = arrayOfItems[position].title
        holder.description.text = arrayOfItems[position].description
        holder.price.text = arrayOfItems[position].price+" DA"
        holder.type.text = arrayOfItems[position].type.toString()
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

    fun filterBy(text: String, arrayOfItem:ArrayList<Item>)
    {
        val listfilter = arrayListOf<Item>()
        if (text.isBlank())
        {
            filterlist(arrayOfItem)
            return
        }

        for (item in arrayOfItem)
        {
            if (item.title.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))||item.price.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault())))
            {
                listfilter.add(item)
            }
        }
        filterlist(listfilter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterlist(filterlist:ArrayList<Item>)
    {
        arrayOfItems = filterlist
        notifyDataSetChanged()
    }

}

class ItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    val title = itemView.findViewById<TextView>(R.id.title)
    val description = itemView.findViewById<TextView>(R.id.description)
    val price = itemView.findViewById<TextView>(R.id.price)
    val buMenu = itemView.findViewById<ImageView>(R.id.buMenu)
    val type = itemView.findViewById<TextView>(R.id.type)
}
