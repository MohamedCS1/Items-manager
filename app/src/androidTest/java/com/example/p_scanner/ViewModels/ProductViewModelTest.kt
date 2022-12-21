package com.example.p_scanner.ViewModels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.DAO.ItemDAO
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.Pojo.ItemType
import com.example.p_scanner.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductViewModelTest  {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var productViewModel: ProductViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var database: ItemsDatabase
    private lateinit var itemDAO: ItemDAO

    @Before
    fun setUp(){

        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, ItemsDatabase::class.java).build()
        itemDAO = database.itemDAO()
        lifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        productViewModel = ProductViewModel(lifecycleOwner ,context)
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun insertEmptyNameReturnError()
    {
        runBlockingTest {

            productViewModel.itemLiveData.value =  Item("5436344654", "Any Product", "Some Description", "546564.345", ItemType.PRODUCT)
            assertThat(productViewModel.itemLiveData.observeForever {

            }.getOrAwaitValue().price == "5465664.345")
        }
    }
}