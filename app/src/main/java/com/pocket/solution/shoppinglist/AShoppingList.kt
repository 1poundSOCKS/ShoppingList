package com.pocket.solution.shoppinglist

class ShoppingListItem(val Name: String) {

    val quantity = 1
}

fun createNewShoppingList(Name: String): List<ShoppingListItem> {

    val item = ShoppingListItem(Name)
    return listOf(item)
}

fun addToShoppingList(Name: String, OldList: List<ShoppingListItem>): List<ShoppingListItem> {

    val item = ShoppingListItem(Name)
    return OldList + listOf(item)
}
