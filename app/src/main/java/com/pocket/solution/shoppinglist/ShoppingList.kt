package com.pocket.solution.shoppinglist

import com.google.gson.GsonBuilder

class ShoppingListData(val items : ShoppingListItems)

data class ShoppingListItems(val items : ArrayList<ShoppingListItem>)

data class ShoppingListItem(val name: String, val quantity : Int)

fun createShoppingListItems(itemNames : Array<String>) : ShoppingListItems =
    ShoppingListItems(ArrayList<ShoppingListItem>(itemNames.map { itemName : String -> ShoppingListItem(itemName, 1) }))

fun createShoppingList(itemNames : Array<String>): ShoppingListData =
    ShoppingListData(createShoppingListItems(itemNames))

fun createEmptyShoppingList(): ShoppingListData =
        createShoppingList(emptyArray())

fun addItemToShoppingList(name: String, data: ShoppingListData) =
    data.items.items.add(ShoppingListItem(name, 1))

fun <E> serializeArrayListAsJson(data: List<E>): String {
    val builder = com.google.gson.GsonBuilder().setPrettyPrinting().create()
    return builder.toJson(data)
}

fun <E> deserializeArrayListFromJson(data: String): ArrayList<E> =
    GsonBuilder().create().fromJson(data, ArrayList<E>().javaClass)

fun serializeShoppingListDataAsJson(data: ShoppingListData): String {
    val names : List<String> = data.items.items.map { item -> item.name }
    return serializeArrayListAsJson(names)
}

fun deserializeShoppingListDataFromJson(data: String): ShoppingListData {
    val names : List<String> = deserializeArrayListFromJson(data)
    val items : List<ShoppingListItem> = names.map { name -> ShoppingListItem(name, 1) }
    return ShoppingListData(ShoppingListItems(ArrayList<ShoppingListItem>(items)))
}
