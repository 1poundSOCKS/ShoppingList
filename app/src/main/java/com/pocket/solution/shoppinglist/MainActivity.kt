package com.pocket.solution.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: MyViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        adapter = MyViewAdapter()
        adapter.createTouchHelper(mobileList)

        mobileList.setHasFixedSize(true)
        mobileList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mobileList.adapter = adapter

        fab.setOnClickListener { view ->
            adapter.addItem("")
        }

        loadAppDataFromFile()
    }

    override fun onStop() {
        super.onStop()
        saveAppDataToFile()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.clear_list -> clearList()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        val loadDataString = sp.getString("Test", null)
    }

    private fun saveAppData() {
        val sp = getSharedPreferences("ShoppingListData", MODE_PRIVATE)
        val spe = sp.edit()
        spe.putString("Test", "Version 1.0")
        spe.commit()
    }

    fun listToString(data: List<String>) = data.fold("", { acc, string -> acc + string } )

    fun stringToList(data: String) : List<String> {
        val jsonRecords = data.split("}{")
        val nonEmptyRecords = jsonRecords.filter { record -> record.isNotEmpty() }
        return nonEmptyRecords.map {
            var newRecord = ""
            if (it[0] != '{') newRecord += "{"
            newRecord += it
            if (it.reversed()[0] != '}') newRecord += "}"
            newRecord
        }
    }

    private fun saveAppDataToFile() {
        try {
            val data = adapter.save()
            val concatenatedData = listToString(data)
            val outputStream = applicationContext.openFileOutput("default", android.content.Context.MODE_PRIVATE)
            outputStream.write(concatenatedData.toByteArray())
        }
        catch(e : java.io.FileNotFoundException ) {}
        catch(e : java.io.IOException) {}
    }

    private fun loadAppDataFromFile() {
        try {
            val inputStream = applicationContext.openFileInput("default")
            val inputData = inputStream.readBytes()
            val inputDataString: String = String(inputData)
            val inputDataRecords = stringToList(inputDataString)
            adapter.load(inputDataRecords)
        }
        catch(e : java.io.FileNotFoundException) {}
        catch(e : java.io.IOException) {}
    }

    private fun clearList() : Boolean {
        adapter.clearList()
        return true
    }
}
