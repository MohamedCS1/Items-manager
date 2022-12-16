package com.example.p_scanner

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.DAO.ItemDAO
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.Pojo.ItemType
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ItemDatabaseTest:TestCase() {

    private lateinit var database: ItemsDatabase
    private lateinit var dao: ItemDAO
    private lateinit var context: Context

    @Before
    public override fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ItemsDatabase::class.java).build()
        dao = database.productDAO()
        super.setUp()
    }

    @After
    fun closeDB() {
        database.close()
    }


    @Test
    fun writeAndRedDB() {
        runBlocking {
            val id = "3454354"
            val item = Item(id, "Any Product", "Some Description", "54623.678", ItemType.PRODUCT)
            database.queryExecutor.execute {
                dao.insertProduct(item)
            }

            val latch = CountDownLatch(1)
                var price:String?=null

                Handler(Looper.getMainLooper()).post {
                    dao.getItemById(id).observeForever(object : Observer<Item> {
                        override fun onChanged(item: Item?) {
                            price = item?.price.toString()
                            latch.countDown()
                        }
                    })
                }

            latch.await(2, TimeUnit.SECONDS)

            assertThat(price == "54623.678").isTrue()
        }

    }
}