package com.pocket.solution.shoppinglist

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun createEmptyShoppingList_works() {

        val list = ShoppingListData.create(emptyArray())
        assertEquals(list.items.items.size, 0)
    }

    @Test
    fun addNewItemToShoppingList_works() {

        val list = ShoppingListData.create(emptyArray())
        assertEquals(list.items.items, listOf<ShoppingListItem>())

        list.addItem("bananas")
        assertEquals(list.items.items, listOf(
                ShoppingListItem("bananas")))

        list.addItem("eggs")
        assertEquals(list.items.items, listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs")))

        list.addItem("chicken")
        assertEquals(list.items.items, listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs"),
                ShoppingListItem("chicken")))
    }

    @Test
    fun testJsonStringLoad() {
        val listOfItems = "[\"bananas\", \"eggs\", \"paper\"]".convertToArrayList<String>()
        assertEquals(listOfItems, listOf("bananas", "eggs", "paper"))
    }

    private fun testShoppingListPersistence(data : ShoppingListData) {
        val serializedData = data.serialize()
        val deserializedData = ShoppingListData.create(serializedData)
        val success = (data.items == deserializedData.items)
        assertTrue("ShoppingListData serialisation unsuccessful", success)
    }

    @Test
    fun emptyShoppingList_works() {
        val data = ShoppingListData.create(emptyArray())
        testShoppingListPersistence(data)
    }

    @Test
    fun singleItemShoppingList_works() {
        val data = ShoppingListData.create(arrayOf("bananas"))
        testShoppingListPersistence(data)
    }

    @Test
    fun manyItemShoppingList_works() {
        val data = ShoppingListData.create(arrayOf("bananas", "eggs", "paper", "berries", "chicken", "tea bags", "milk"))
        testShoppingListPersistence(data)

        val reverseData = ShoppingListData.create(arrayOf("milk", "tea bags", "chicken", "berries", "paper", "eggs", "bananas"))
        testShoppingListPersistence(reverseData)

        assertFalse(data == reverseData)
    }

    @Test
    fun selectShoppingListItem_works() {
        val data = ShoppingListData.create(arrayOf("bananas", "eggs", "paper"))
        assertTrue(data.selectItem(0))
        assertTrue(data.selectItem(1))
        assertTrue(data.selectItem(2))
        assertFalse(data.selectItem(0))
        assertTrue(data.selectItem(0))
        assertFalse(data.selectItem(2))
        assertTrue(data.selectItem(2))
        assertFalse(data.selectItem(1))
        assertTrue(data.selectItem(1))
    }
}
