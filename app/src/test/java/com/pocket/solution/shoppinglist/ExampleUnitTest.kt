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

        val data = ShoppingListData.create(emptyArray())
        assertTrue(data.isEmpty)
    }

    @Test
    fun addNewItemToShoppingList_works() {

        val data = ShoppingListData.create(emptyArray())
        data.addItem("bananas")
        assertEquals(data.items.items, listOf(
                ShoppingListItem("bananas")))

        data.addItem("eggs")
        assertEquals(data.items.items, listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs")))

        data.addItem("chicken")
        assertEquals(data.items.items, listOf(
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

    @Test
    fun deleteWithNothingSelected_works() {
        val data = ShoppingListData.create(arrayOf("bananas", "eggs", "paper"))
        data.deleteSelectedItems()
        assertTrue(data.items.items == listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs"),
                ShoppingListItem("paper")))
    }

    @Test
    fun deleteOnlyItem_works() {
        val data = ShoppingListData.create(arrayOf("bananas"))
        data.selectItem(0)
        data.deleteSelectedItems()
        assertTrue(data.isEmpty)
    }

    @Test
    fun deleteLastItem_works() {
        val data = ShoppingListData.create(arrayOf("bananas", "eggs", "paper"))
        data.selectItem(2)
        data.deleteSelectedItems()
        assertTrue(data.items.items == listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs")))
    }
}
