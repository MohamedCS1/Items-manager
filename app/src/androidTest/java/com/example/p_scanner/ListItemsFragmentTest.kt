package com.example.p_scanner

import android.app.Activity
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import com.example.p_scanner.adapters.ItemViewHolder
import com.example.p_scanner.adapters.ItemsAdapter
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import com.example.p_scanner.repository.Repository
import com.example.p_scanner.ui.listItems.ListItemsFragment
import com.example.p_scanner.ui.main.MainActivity
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ListItemsFragmentTest{


    lateinit var scenario: FragmentScenario<ListItemsFragment>

    val positionClicking = 0


    val itemsInTest = arrayListOf<Item>(Item("0","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("1","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("2","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("0","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("1","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("2","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE))

    lateinit var context:Context


    @Before
    fun before() {
        scenario = FragmentScenario.Companion.launchInContainer(ListItemsFragment::class.java)
        scenario.withFragment {
            adapter.setList(itemsInTest)
        }
        context = ApplicationProvider.getApplicationContext<Context>()
        Thread.sleep(1000)
    }

    @Test
    fun testIsListItemsFragmentVisible()
    {
        onView(withId(R.id.rv_items)).perform(RecyclerViewActions.actionOnItemAtPosition<ItemViewHolder>(positionClicking,click()))
        onView(withId(R.id.rv_items))
            .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Text of item you want to scroll to")), click()))
    }
}