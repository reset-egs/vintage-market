package com.example.vintagemarket.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vintagemarket.R
import com.example.vintagemarket.models.Item

class ItemListAdapter(private val items: List<Item>,
                      private val onItemClicked: (id: Int) -> Unit) :
    RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View, private val onItemClicked: (id: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val itemDescription: TextView = itemView.findViewById(R.id.item_description)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val id = bindingAdapterPosition
            onItemClicked(id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_layout, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Bind item data to ViewHolder views
        holder.itemDescription.text = item.description
        holder.itemPrice.text = item.price.toString() + " kr."
    }

    override fun getItemCount() = items.size
}
