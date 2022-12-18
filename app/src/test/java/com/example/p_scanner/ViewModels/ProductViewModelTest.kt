package com.example.p_scanner.ViewModels

import android.content.Context
import androidx.lifecycle.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p_scanner.Pojo.Item
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ProductViewModelTest {

    private lateinit var productViewModel: ProductViewModel
    lateinit var context: Context
    lateinit var lifecycle: LifecycleOwner

    @Before
    fun setUp(){

        lifecycle = Mockito.mock(LifecycleOwner::class.java)
        context = ApplicationProvider.getApplicationContext()
        productViewModel = ProductViewModel(lifecycle ,context)

    }

    @Test
    fun insertItemWithEmptyNameReturnError()
    {
        val latch = CountDownLatch(1)
        productViewModel.setValueToListItems(emptyList())
        productViewModel.listItemsLiveData.observeForever(object : Observer<List<Item>> {
            override fun onChanged(listItems: List<Item>?) {
                Truth.assertThat(listItems).isNotEmpty()
                latch.countDown()
            }
        })
        latch.await(2, TimeUnit.SECONDS)
    }


}
