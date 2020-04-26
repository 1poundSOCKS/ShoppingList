package com.pocket.solution.shoppinglist

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class MyAdapter() :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var items: ShoppingListData = createEmptyShoppingList()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class MyViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(itemName : String)
        {
            v.textView.text = itemName
            v.setOnClickListener { v.textView.setTextColor(Color.BLUE) }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_row, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(items.items.items[position].name)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.items.items.size

    fun addItem(itemName : String) {
        addItemToShoppingList(itemName, items)
        notifyItemRangeChanged(items.items.items.lastIndex, 1)
    }

    fun saveAsJson() : String {
        return serializeShoppingListDataAsJson(items)
    }

    fun loadFromJson(data : String) {
        items = deserializeShoppingListDataFromJson(data)
        notifyDataSetChanged()
    }
}
