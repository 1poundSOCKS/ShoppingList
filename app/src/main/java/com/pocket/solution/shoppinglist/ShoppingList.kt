package com.pocket.solution.shoppinglist

import com.google.gson.GsonBuilder

fun <E> String.convertToArrayList() : ArrayList<E> = GsonBuilder().create().fromJson(this, ArrayList<E>().javaClass)

class ShoppingListData() {

    val items = ArrayList<ShoppingListItem>(emptyList())

    val isEmpty : Boolean
        get() = items.size == 0

    fun addItem(name: String) = items.add(ShoppingListItem(name, 1))

    fun serialize(): String = convertNamesToJson()

    fun selectItem(index: Int): Boolean {
        return selectItem(items[index])
    }

    fun getIndexedItems(): List<IndexedShoppingListItem> {
        var index = 0
        return ArrayList<IndexedShoppingListItem>(items.map { item -> IndexedShoppingListItem(index++, item) })
    }

    fun getSelectedItems() : List<IndexedShoppingListItem> = getIndexedItems().filter { indexedItem -> indexedItem.item.selected }

    fun deleteSelectedItems() {
        val remainingItems = items.filter { item -> !item.selected }
        items.clear()
        items.addAll(remainingItems)
    }

    fun deleteLastSelectedItem() : IndexedShoppingListItem? {
        val selectedItems = getSelectedItems()
        try {
            items.removeAt(selectedItems.last().index)
        }
        catch( e: NoSuchElementException ) {
            return null
        }
        return selectedItems.last()
    }

    fun selectAllItems() {
        for ( item in items ) {
            item.selected = true
        }
    }

    fun getItem(index: Int) : ShoppingListItem = items[index]

    fun load(itemNames : Array<String>) {
        val itemsToLoad = ArrayList<ShoppingListItem>(itemNames.map { itemName: String -> ShoppingListItem(itemName, 1) })
        load(itemsToLoad)
    }

    fun load(itemsAsJson : String) {
        val names: List<String> = itemsAsJson.convertToArrayList()
        val itemsToLoad: List<ShoppingListItem> = names.map { name -> ShoppingListItem(name, 1) }
        load(itemsToLoad)
    }

    fun load(itemsToLoad : List<ShoppingListItem>) {
        items.clear()
        items.addAll(itemsToLoad)
    }

    fun save(f: (String) -> Boolean ) {
        val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
        for( item in items ) {
            f(gsonBuilder.toJson(item))
        }
    }

    fun serializeAsJson() : List<String> {
        val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
        val serializedData = arrayListOf<String>()
        for( item in items ) {
            serializedData.add((gsonBuilder.toJson(item)))
        }
        return serializedData
    }

    fun loadFromJson(data: List<String>) {
        items.clear()
        val gsonBuilder = GsonBuilder().create()
        for( record in data ) {
            val nextItem = gsonBuilder.fromJson(record, ShoppingListItem("").javaClass)
            items.add(nextItem)
        }
    }

    private fun convertNamesToJson() : String = GsonBuilder().setPrettyPrinting().create().toJson(items.map { item -> item.name })

    companion object {
        fun selectItem(item: ShoppingListItem): Boolean {
            item.selected = !item.selected
            return item.selected
        }
    }
}

data class ShoppingListItem(var name: String, var quantity : Int = 1, var selected: Boolean = false)

data class IndexedShoppingListItem(val index: Int, val item: ShoppingListItem)
