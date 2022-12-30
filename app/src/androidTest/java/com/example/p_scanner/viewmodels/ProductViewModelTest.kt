package com.example.p_scanner.viewmodels

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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductViewModelTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()



    private lateinit var context: Context
    private lateinit var productViewModel: ProductViewModel
    private lateinit var database: ItemsDatabase
    private lateinit var itemDAO: ItemDAO

    @Before
     fun setUp(){

        context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ItemsDatabase::class.java).build()
        itemDAO = database.itemDAO()
        productViewModel = ProductViewModel(context)
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun testViewModel()
    {
        runBlockingTest {

          productViewModel.itemLiveData.value =  Item("67576", "Any Product", "Some Description", "6776567", ItemType.PRODUCT)
          val it = productViewModel.itemLiveData.getOrAwaitValue()
            assertThat(it!!.price == "6776567").isTrue()
        }
    }
}