package com.pocket.solution.shoppinglist

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class MyAdapter() :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var data: ShoppingListData = ShoppingListData.create(emptyArray())

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class MyViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item : ShoppingListItem)
        {
            v.textView.text = item.name
            v.textView.setTextColor(Color.BLACK)
            v.textView2.text = item.quantity.toString()
            v.textView2.setTextColor(Color.BLUE)
            v.setOnClickListener {
                if ( item.select() ) {
                    v.textView.setBackgroundColor(Color.LTGRAY)
                }
                else {
                    v.textView.setBackgroundColor(Color.WHITE)
                }
            }
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
        //holder.bind(data.itemName(position))
        holder.bind(data.items.getItem(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.itemCount

    fun addItem(itemName : String) {
        data.addItem(itemName)
        //notifyItemRangeChanged(data.lastItemIndex, 1)
        notifyDataSetChanged()
    }

    fun deleteSelectedItems() {
        data.deleteSelectedItems()
        notifyDataSetChanged()
    }

    fun saveAsJson() : String = data.serialize()

    fun loadFromJson(jsonData : String) {
        data = ShoppingListData.create(jsonData)
        notifyDataSetChanged()
    }

    private val ShoppingListData.lastItemIndex: Int
        get() = items.items.lastIndex

    private val ShoppingListData.itemCount: Int
        get() = items.items.size

    private fun ShoppingListData.itemName(pos : Int) = items.items[pos].name
}
