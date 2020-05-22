package com.pocket.solution.shoppinglist

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class MyViewAdapter() :
        RecyclerView.Adapter<MyViewAdapter.MyViewHolder>() {

    lateinit var touchHelper: ItemTouchHelper

    class MyViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v)

    private var recyclerView : RecyclerView? = null
    private var data = ShoppingListData()
    var focusItem = -1

    fun createTouchHelper(mobileList: RecyclerView) {
        touchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
        touchHelper.attachToRecyclerView(mobileList)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView): Unit {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = data.getItem(holder.adapterPosition)
        val v = holder.v
        v.textView.text = Editable.Factory.getInstance().newEditable(item.name)
        v.textView.setTextColor(Color.BLACK)
        if ( holder.adapterPosition == focusItem ) {
            v.textView.requestFocus()
            focusItem = -1
        }

        v.checkbox_selected.isChecked = item.selected

        v.textView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            true
        }

        v.textView.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                data.getItem(holder.adapterPosition).name = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    fun moveRow(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                data.swapItems(i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                data.swapItems(i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun deleteRow(position: Int, direction: Int) {
        data.deleteItem(position)
        notifyItemRemoved(position)
    }

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

    fun save() : List<String> = data.serializeAsJson()

    fun load(serializedData: List<String>) {
        data.loadFromJson(serializedData)
        notifyDataSetChanged()
    }

    fun loadTestData() {
        data.load(arrayOf("1", "2","3","4","5","6","7","8","9","10","11"))
    }
}

class ItemTouchHelperCallback(private val adapter: MyViewAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.moveRow(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is MyViewAdapter.MyViewHolder) {
                //adapter.selectRow(viewHolder)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is MyViewAdapter.MyViewHolder) {
            //adapter.clearRow(viewHolder)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.deleteRow(viewHolder.adapterPosition, direction)
    }
}
