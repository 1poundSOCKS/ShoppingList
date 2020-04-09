package com.pocket.solution.shoppinglist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.content.SharedPreferences

class MyAdapter() :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val myDataset: ArrayList<String> = ArrayList<String>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class MyViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = v.findViewById(R.id.textView)
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
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun addItem(itemName : String) {
        myDataset.add(itemName)
        //notifyItemRangeChanged(myDataset.lastIndex, 1)
        notifyDataSetChanged()
    }

    fun save(spe : SharedPreferences.Editor, name : String) {
        val saveData = HashSet<String>()
        saveData.addAll(myDataset)
        spe.putStringSet(name, saveData)
        spe.commit()
    }

    fun load(sp : SharedPreferences, name : String) {
        val loadData = sp.getStringSet(name, null)
        if (loadData != null) {
            myDataset.clear()
            myDataset.addAll(loadData)
            notifyDataSetChanged()
        }
    }
}
