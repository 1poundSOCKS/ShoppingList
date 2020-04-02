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
    fun createNewShoppingList_works() {

        val list = createNewShoppingList("bananas")
        assertEquals(list.size, 1)
        assertEquals(list[0].Name,"bananas")
    }

    @Test
    fun addNewItemToShoppingList_works() {

        val list = createNewShoppingList("bananas")
        assertEquals(list.size, 1)
        assertEquals(list[0].Name,"bananas")

        val list2 = addToShoppingList("eggs", list)
        assertEquals(list2.size, 2)
        assertEquals(list2[0].Name,"bananas")
        assertEquals(list2[1].Name,"eggs")

        val list3 = addToShoppingList("chicken", list2)
        assertEquals(list3.size, 3)
        assertEquals(list3[0].Name,"bananas")
        assertEquals(list3[1].Name,"eggs")
        assertEquals(list3[2].Name,"chicken")
    }
}
