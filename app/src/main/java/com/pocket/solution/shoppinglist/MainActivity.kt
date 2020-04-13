package com.pocket.solution.shoppinglist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.widget.ListView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.content.SharedPreferences

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_send.setOnClickListener {
            Log.d("MainActivity", "Add clicked")
            val newItemName : String = editText.text.toString()
            adapter.addItem(newItemName)
            editText.text.clear()
        }
/*
        button_save.setOnClickListener {
            Log.d("MainActivity", "Save clicked")
            val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
            val spe = sp.edit()
            adapter.save(spe, "Items")
        }

        button_load.setOnClickListener {
            Log.d("MainActivity", "Load clicked")
            val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
            adapter.load(sp, "Items")
        }

        button_save_json.setOnClickListener {
            Log.d("MainActivity", "Save Json clicked")
            val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
            val spe = sp.edit()
            adapter.saveAsJson(spe, "ItemsAsJson")
        }

        button_load_json.setOnClickListener {
            Log.d("MainActivity", "Load Json clicked")
            val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
            adapter.loadFromJson(sp, "ItemsAsJson")
        }
*/
        mobileList.setHasFixedSize(true)
        mobileList.layoutManager = LinearLayoutManager(this)
        mobileList.adapter = adapter

        loadAppData()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun loadAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        adapter.loadFromJson(sp, "ItemsAsJson")
    }

    fun saveAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        val spe = sp.edit()
        adapter.saveAsJson(spe, "ItemsAsJson")
    }
}
