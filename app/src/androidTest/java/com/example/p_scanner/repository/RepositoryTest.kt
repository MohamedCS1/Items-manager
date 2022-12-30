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
    fun insertBlankTitleOrDescriptionOrPriceReturnFalse() {
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


}
