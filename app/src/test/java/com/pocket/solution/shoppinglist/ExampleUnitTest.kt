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

        val data = ShoppingListData()
        assertTrue(data.isEmpty)
    }

    @Test
    fun addNewItemToShoppingList_works() {

        val data = ShoppingListData()
        data.addItem("bananas")
        assertEquals(data.items, listOf(
                ShoppingListItem("bananas")))

        data.addItem("eggs")
        assertEquals(data.items, listOf(
                ShoppingListItem("bananas"),
                ShoppingListItem("eggs")))

        data.addItem("chicken")
        assertEquals(data.items, listOf(
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
        val deserializedData = ShoppingListData()
        deserializedData.load(serializedData)
        val success = (data.items == deserializedData.items)
        assertTrue("ShoppingListData serialisation unsuccessful", success)
    }

/*
    @Test
    fun testSaveAndLoadToJson_works() {
        val data = ShoppingListData()
        data.load(arrayOf("bananas", "eggs", "paper", "berries", "chicken", "tea bags", "milk"))
        val savedData = ArrayList<String>()
        data.save() { nextLine -> savedData.add(nextLine) }

        val dataCopy = ShoppingListData()
        dataCopy.load() {
            if( savedData.size == 0)
                null
            else {
                savedData[0]
                savedData.removeAt(0)
            }
        }

        assertEquals("data persisted", data.items, dataCopy.items)
    }
*/

    @Test
    fun testSaveAndLoadToJson_works() {
        val data = ShoppingListData()
        data.load(arrayOf("bananas", "eggs", "paper", "berries", "chicken", "tea bags", "milk"))

        val serializedData = data.serializeAsJson()

        val stringData = MainActivity().listToString(serializedData)
        val listData = MainActivity().stringToList(stringData)

        val dataCopy = ShoppingListData()
        dataCopy.loadFromJson(listData)

        assertEquals("data persisted", data.items, dataCopy.items)
    }

    @Test
    fun emptyShoppingList_works() {
        val data = ShoppingListData()
        testShoppingListPersistence(data)
    }

    @Test
    fun singleItemShoppingList_works() {
        val data = ShoppingListData()
        data.load(arrayOf("bananas"))
        testShoppingListPersistence(data)
    }

    @Test
    fun manyItemShoppingList_works() {
        val data = ShoppingListData()
        data.load(arrayOf("bananas", "eggs", "paper", "berries", "chicken", "tea bags", "milk"))
        testShoppingListPersistence(data)

        val reverseData = ShoppingListData()
        reverseData.load(arrayOf("milk", "tea bags", "chicken", "berries", "paper", "eggs", "bananas"))
        testShoppingListPersistence(reverseData)

        assertFalse(data == reverseData)
    }

    @Test
    fun selectShoppingListItem_works() {
        val data = ShoppingListData()
        data.load(arrayOf("bananas", "eggs", "paper"))
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
    fun deleteSelectedItems_works() {
        val data = ShoppingListData()
        data.deleteSelectedItems()
        assertTrue("empty list", data.isEmpty)

        data.load(listOf(ShoppingListItem("bananas", 1, false)))
        data.deleteSelectedItems()
        assertTrue("no items selected",
                data.items == listOf(ShoppingListItem("bananas")))

        data.load(listOf(ShoppingListItem("bananas", 1, true)))
        data.deleteSelectedItems()
        assertTrue("delete only item", data.isEmpty)

        data.load(listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, true),
                ShoppingListItem("tea bags", 1, true)))
        data.deleteSelectedItems()
        assertTrue("delete all items", data.isEmpty)

        data.load(listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, false),
                ShoppingListItem("tea bags", 1, false)))
        data.deleteSelectedItems()
        assertTrue("delete first item",
                data.items == listOf(
                        ShoppingListItem("eggs", 1, false),
                        ShoppingListItem("tea bags",1, false)))

        data.load(listOf(
                ShoppingListItem("bananas", 1, false),
                ShoppingListItem("eggs", 1, true),
                ShoppingListItem("tea bags", 1, false)))
        data.deleteSelectedItems()
        assertTrue("delete last item",
                data.items == listOf(
                        ShoppingListItem("bananas", 1, false),
                        ShoppingListItem("tea bags",1, false)))

        data.load(listOf(
                ShoppingListItem("bananas", 1, false),
                ShoppingListItem("eggs", 1, true),
                ShoppingListItem("tea bags", 1, false)))
        data.deleteSelectedItems()
        assertTrue("delete middle item",
                data.items == listOf(
                        ShoppingListItem("bananas", 1, false),
                        ShoppingListItem("tea bags",1, false)))
    }

    @Test
    fun getSelectedItems_works() {
        val data = ShoppingListData()
        assertTrue("empty list", data.getSelectedItems().isEmpty())

        data.load(listOf(
                ShoppingListItem("bananas", 1, false),
                ShoppingListItem("eggs", 1, false),
                ShoppingListItem("tea bags", 1, false))
        )
        assertTrue("no selected items", data.getSelectedItems().isEmpty())

        data.load(listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, true),
                ShoppingListItem("tea bags", 1, true)))
        assertTrue("all items selected", data.getSelectedItems() == listOf(
                IndexedShoppingListItem(0, ShoppingListItem("bananas", 1, true)),
                IndexedShoppingListItem(1, ShoppingListItem("eggs", 1, true)),
                IndexedShoppingListItem(2, ShoppingListItem("tea bags",1, true)))
        )

        data.load(listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, false),
                ShoppingListItem("tea bags", 1, false)))
        assertTrue("first item selected", data.getSelectedItems() == listOf(
                IndexedShoppingListItem(0, ShoppingListItem("bananas", 1, true)))
        )

        data.load(listOf(
                ShoppingListItem("bananas", 1, false),
                ShoppingListItem("eggs", 1, false),
                ShoppingListItem("tea bags", 1, true)))
        assertTrue("last item selected", data.getSelectedItems() == listOf(
                IndexedShoppingListItem(2, ShoppingListItem("tea bags", 1, true)))
        )
    }

    @Test
    fun deleteLastSelectedItem_works() {
        val data = ShoppingListData()
        assertTrue("empty list", data.deleteLastSelectedItem() == null)

        data.load(listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, true),
                ShoppingListItem("tea bags", 1, true)))

        var deletedItem = data.deleteLastSelectedItem()
        assertTrue("return last item", deletedItem == IndexedShoppingListItem(2, ShoppingListItem("tea bags", 1, true)))
        assertTrue("item deleted", data.items == listOf(
                ShoppingListItem("bananas", 1, true),
                ShoppingListItem("eggs", 1, true)))
    }
}
