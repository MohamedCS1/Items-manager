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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
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
        val lifecycle = mock(LifecycleOwner::class.java)
        val id = "jklhklj"
        val item = Item(id, "Any Product", "Some Description", "231gh4,0", ItemType.PRODUCT)
        database.queryExecutor.execute {
            dao.insertProduct(item)
        }

        Handler(Looper.getMainLooper()).post {
            dao.getAllItems().observeForever(object : Observer<List<Item>> {
                override fun onChanged(price: List<Item>?) {

                    Log.d("item",  price!![0].price)
                    assertThat(price[0].price == "2314,0").isTrue()
                }
            })
        }

//            Handler(Looper.getMainLooper()).post {
//                dao.getItemById(id).observe(lifecycle, object : Observer<Item> {
//                    override fun onChanged(price: Item?) {
//
//                        Log.d("price", "fffffffffff" + price)
//                        assertThat(price?.price == "2314,0").isTrue()
//                    }
//                })
//            }

    }
}