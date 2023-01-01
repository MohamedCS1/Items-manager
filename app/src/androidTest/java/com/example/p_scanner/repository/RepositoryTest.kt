package com.example.p_scanner.repository


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.getOrAwaitValue
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: Repository
    lateinit var itemDao: ItemDAO
    lateinit var context: Context
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        itemDao = Room.inMemoryDatabaseBuilder(context ,ItemsDatabase::class.java).build().itemDAO()
        repository = Repository(itemDao)
    }

    @Test
    fun testEnterBlankInformationReturnFalse() {
        runBlockingTest {
            val id = "34554343"
            val itemInserted = repository.insertItem(
                Item(
                    id,
                    "",
                    "",
                    "",
                    ItemType.PRODUCT
                )
            )
            assertThat(itemInserted).isFalse()
        }
    }

    @Test
    fun testWriteAndRead() {
        runBlockingTest {
            val id = "34554343"
            repository.insertItem(
                Item(id, "Any Title", "Any Description", "12345", ItemType.PRODUCT)
            )
            val getItem = repository.getItemById(id).getOrAwaitValue()
            assertThat(getItem.title == "Any Title").isTrue()
            assertThat(getItem.description =="Any Description").isTrue()
            assertThat(getItem.price == "12345").isTrue()
        }
    }

    @Test
    fun testGetAllItems()
    {
        runBlockingTest {
            repository.insertItem(
                Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(
                Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(
                Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))

           assertThat(repository.getAllItems().getOrAwaitValue()).containsExactly(Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT) ,Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT) ,Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT) ,Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))
        }
    }

    @Test
    fun testUpdateItemById()
    {
        runBlockingTest {
            val id = "456564"
            repository.insertItem(Item(id ,"Any Title" ,"Any Description" ,"23500" ,ItemType.PRODUCT))
            repository.updateItemById(id ,"Updated Title" ,"Updated Description" ,"29000" ,ItemType.SERVICE)

            assertThat(repository.getItemById(id).getOrAwaitValue().title == "Updated Title").isTrue()
            assertThat(repository.getItemById(id).getOrAwaitValue().description == "Updated Description").isTrue()
            assertThat(repository.getItemById(id).getOrAwaitValue().price == "29000").isTrue()
            assertThat(repository.getItemById(id).getOrAwaitValue().type == ItemType.SERVICE).isTrue()
        }
    }

    @Test
    fun testDeleteItemById()
    {
        runBlockingTest{
            repository.insertItem(
                Item("0", "Title 0", "Description 0", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(
                Item("1", "Title 1", "Description 1", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(
                Item("2", "Title 2", "Description 2", "12345", ItemType.PRODUCT)
            )
            repository.insertItem(Item("3", "Title 3", "Description 3", "12345", ItemType.PRODUCT))

            repository.deleteItemById("2")

            assertThat(repository.getItemById("2").getOrAwaitValue()).isNull()
        }
    }

    @Test
    fun testItemIsExists()
    {
        runBlockingTest {
            val id = "2"
            repository.insertItem(Item(id, "Title 2", "Description 2", "12345", ItemType.PRODUCT))
            assertThat(repository.itemIsExists(id)).isTrue()
        }
    }


}
