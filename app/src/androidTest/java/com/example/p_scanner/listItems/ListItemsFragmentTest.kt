package com.example.p_scanner.listItems

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.withFragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.p_scanner.R
import com.example.p_scanner.Utils.atPosition
import com.example.p_scanner.adapters.ItemViewHolder
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import com.example.p_scanner.ui.listItems.ListItemsFragment
import org.junit.Before
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
        onView(withId(R.id.rv_items)).check(matches(atPosition(1, hasDescendant(withText(itemsInTest[0].title)))))
    }
}