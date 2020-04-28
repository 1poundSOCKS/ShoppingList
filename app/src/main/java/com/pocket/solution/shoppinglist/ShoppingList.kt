package com.pocket.solution.shoppinglist

import com.google.gson.GsonBuilder

fun <E> String.convertToArrayList() : ArrayList<E> = GsonBuilder().create().fromJson(this, ArrayList<E>().javaClass)

class ShoppingListData(val items : ShoppingListItems) {
    fun addItem(name: String) = items.items.add(ShoppingListItem(name, 1))
    fun serialize(): String = items.convertNamesToJson()
    fun selectItem(index: Int): Boolean = items.selectItem(index)

    companion object {
        fun create(itemNames : Array<String>): ShoppingListData =
                ShoppingListData(ShoppingListItems.create(itemNames))

        fun create(data: String): ShoppingListData =
                ShoppingListData(ShoppingListItems.create(data))
    }
}

data class ShoppingListItems(val items : ArrayList<ShoppingListItem>) {
    companion object {
        fun create(itemNames: Array<String>): ShoppingListItems =
                ShoppingListItems(ArrayList<ShoppingListItem>(itemNames.map { itemName: String -> ShoppingListItem(itemName, 1) }))

        fun create(data: String) : ShoppingListItems {
            val names: List<String> = data.convertToArrayList()
            val items: List<ShoppingListItem> = names.map { name -> ShoppingListItem(name, 1) }
            return ShoppingListItems(ArrayList<ShoppingListItem>(items))
        }
    }
}

fun ShoppingListItems.convertNamesToJson() : String = GsonBuilder().setPrettyPrinting().create().toJson(this.items.map { item -> item.name })
fun ShoppingListItems.getItem(index: Int) : ShoppingListItem = items[index]
fun ShoppingListItems.selectItem(index: Int): Boolean = items[index].select()

data class ShoppingListItem(val name: String, val quantity : Int = 1, var selected: Boolean = false)

fun ShoppingListItem.select(): Boolean {
    selected = !selected
    return selected
}
