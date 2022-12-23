package com.example.p_scanner

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
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
        runBlockingTest {
            val id = "8986798"
            val item = Item(id, "Any Product", "Some Description", "54623.678", ItemType.PRODUCT)
            dao.insertItem(item)
            assertThat(dao.getItemById(id).getOrAwaitValue().price == "54623.678").isTrue()
        }

    }
}