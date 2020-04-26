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

        val list = createEmptyShoppingList()
        assertEquals(list.items.items.size, 0)
    }

    @Test
    fun addNewItemToShoppingList_works() {

        val list = createEmptyShoppingList()
        assertEquals(list.items.items.size, 0)

        addItemToShoppingList("bananas", list)
        assertEquals(list.items.items.size, 1)
        assertEquals(list.items.items[0].name, "bananas")

        addItemToShoppingList("eggs", list)
        assertEquals(list.items.items.size, 2)
        assertEquals(list.items.items[0].name, "bananas")
        assertEquals(list.items.items[1].name, "eggs")

        addItemToShoppingList("chicken", list)
        assertEquals(list.items.items.size, 3)
        assertEquals(list.items.items[0].name, "bananas")
        assertEquals(list.items.items[1].name, "eggs")
        assertEquals(list.items.items[2].name, "chicken")
    }

    private fun testShoppingListPersistence(data : ShoppingListData) {
        val serializedData = serializeShoppingListDataAsJson(data)
        val deserializedData = deserializeShoppingListDataFromJson(serializedData)
        val success = (data.items == deserializedData.items)
        assertTrue("ShoppingListData serialisation unsuccessful", success)
    }

    @Test
    fun emptyShoppingList_works() {
        val data = createEmptyShoppingList()
        testShoppingListPersistence(data)
    }

    @Test
    fun singleItemShoppingList_works() {
        val data = createShoppingList(arrayOf("bananas"))
        testShoppingListPersistence(data)
    }

    @Test
    fun manyItemShoppingList_works() {
        val data = createShoppingList(arrayOf("bananas", "eggs", "paper", "berries", "chicken", "tea bags", "milk"))
        testShoppingListPersistence(data)

        val reverseData = createShoppingList(arrayOf("milk", "tea bags", "chicken", "berries", "paper", "eggs", "bananas"))
        testShoppingListPersistence(reverseData)

        assertFalse(data == reverseData)
    }
}
