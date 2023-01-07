package com.example.p_scanner

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.Utils.getOrAwaitValue
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ItemDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ItemsDatabase
    private lateinit var dao: ItemDAO
    private lateinit var context: Context

    @Before
    fun setUp() {

        context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ItemsDatabase::class.java).allowMainThreadQueries().build()
        dao = database.itemDAO()
    }

    @After
    fun close() {
        database.close()
    }


    @Test
    fun writeAndRed() {
        runTest {
            val id = "8986798"
            val item = Item(id, "Any Product", "Some Description", "54623.678", ItemType.PRODUCT)
            dao.insertItem(item)
            assertThat(dao.getItemById(id).getOrAwaitValue().price == "54623.678").isTrue()
        }

    }

    @Test
    fun testGetAllItems()
    {
        runTest {
            dao.insertItem(
                Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(
                Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(
                Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))

            assertThat(dao.getAllItems().getOrAwaitValue()).containsExactly(Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT) ,Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT) ,Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT) ,Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))
        }
    }

    @Test
    fun testUpdateItemById()
    {
        runTest {
            val id = "456564"
            dao.insertItem(Item(id ,"Any Title" ,"Any Description" ,"23500" ,ItemType.PRODUCT))
            dao.updateItemById(id ,"Updated Title" ,"Updated Description" ,"29000" ,ItemType.SERVICE)

            assertThat(dao.getItemById(id).getOrAwaitValue().title == "Updated Title").isTrue()
            assertThat(dao.getItemById(id).getOrAwaitValue().description == "Updated Description").isTrue()
            assertThat(dao.getItemById(id).getOrAwaitValue().price == "29000").isTrue()
            assertThat(dao.getItemById(id).getOrAwaitValue().type == ItemType.SERVICE).isTrue()
        }
    }

    @Test
    fun testDeleteItemById()
    {
        runTest{
            dao.insertItem(
                Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(
                Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(
                Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT)
            )
            dao.insertItem(Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))

            dao.deleteItemById("2")

            assertThat(dao.getItemById("2").getOrAwaitValue()).isNull()
        }
    }

    @Test
    fun testItemIsExists()
    {
        runTest {
            val id = "2"
            dao.insertItem(Item(id, "Title 2", "Description 2", "12345", ItemType.PRODUCT))
            assertThat(dao.itemIsExists(id)).isTrue()
        }
    }


}