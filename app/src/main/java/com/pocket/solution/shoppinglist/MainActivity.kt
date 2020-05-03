package com.pocket.solution.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        mobileList.setHasFixedSize(true)
        mobileList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mobileList.adapter = adapter

        fab.setOnClickListener { view ->
            adapter.addItem("")
        }

        loadAppData()
        //adapter.loadTestData()
    }

    override fun onStop() {
        super.onStop()
        saveAppData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> deleteSelectedItems()
            R.id.action_select_all -> selectAllItems()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        val loadDataString = sp.getString("ItemsAsJson", null)
        adapter.loadFromJson(loadDataString)
    }

    private fun saveAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        val spe = sp.edit()
        val data = adapter.saveAsJson()
        spe.putString("ItemsAsJson", data)
        spe.commit()
    }

    private fun deleteSelectedItems() : Boolean {
        adapter.deleteSelectedItems()
        return true
    }

    private fun selectAllItems() : Boolean {
        adapter.selectAllItems()
        return true
    }

    private fun getSelectedItems() : List<Int> {
        val selectedItems = ArrayList<Int>()
        for( childIndex in 0..mobileList.childCount ) {
            val viewHolder = mobileList.getChildViewHolder(mobileList.getChildAt(childIndex))
        }
        return selectedItems
    }
}
