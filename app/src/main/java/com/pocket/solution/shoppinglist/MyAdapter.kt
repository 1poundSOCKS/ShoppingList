package com.pocket.solution.shoppinglist

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class MyAdapter() :
        androidx.recyclerview.widget.RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var recyclerView : RecyclerView? = null
    private var data = ShoppingListData()
    private var focusItem = -1

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class MyViewHolder(private val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

        fun bind(adapter : MyAdapter)
        {
            val item = adapter.data.getItem(adapterPosition)
            v.textView.text = Editable.Factory.getInstance().newEditable(item.name)
            v.textView.setTextColor(Color.BLACK)
            if ( adapterPosition == adapter.focusItem ) {
                v.textView.requestFocus()
                adapter.focusItem = -1
            }
            v.checkbox_selected.isChecked = item.selected
            v.checkbox_selected.setOnClickListener {
                item.selected = !item.selected
                v.checkbox_selected.isChecked = item.selected
            }

            addTextChangeListener(adapter)
        }

        private fun addTextChangeListener(adapter : MyAdapter) {
            v.textView.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    adapter.data.getItem(adapterPosition).name = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                }
            })
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView): Unit {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
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
        holder.bind(this)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.items.size

    fun addItem(itemName : String) : Int {
        data.addItem(itemName)
        focusItem = data.items.lastIndex
        notifyItemInserted(data.items.lastIndex)
        recyclerView?.scrollToPosition(data.items.lastIndex)
        return data.items.lastIndex
    }

    fun deleteSelectedItems() {
        var deletedItem = data.deleteLastSelectedItem()
        while ( deletedItem != null ) {
            notifyItemRemoved(deletedItem.index)
            deletedItem = data.deleteLastSelectedItem()
        }
    }

    fun selectAllItems() {
        data.selectAllItems()
        notifyDataSetChanged()
    }

/*
    fun saveAsJson() : String = data.serialize()

    fun loadFromJson(jsonData : String) {
        data.load(jsonData)
        notifyDataSetChanged()
    }
*/

    fun save() : List<String> = data.serializeAsJson()

    fun load(serializedData: List<String>) {
        data.loadFromJson(serializedData)
        notifyDataSetChanged()
    }

    fun loadTestData() {
        data.load(arrayOf("1", "2","3","4","5","6","7","8","9","10","11"))
    }
}
