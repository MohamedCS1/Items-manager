package com.example.p_scanner

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ListItemsFragmentTest{
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    val positionClicking = 4
    val itemsInTest = arrayListOf<Item>(Item("0","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("1","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE) ,Item("2","Any Title" ,"Any Description" ,"2345" ,ItemType.SERVICE))

    @Test
    fun testIsListItemsFragmentVisible()
    {
        onView(withId(R.id.rv_items)).perform(RecyclerViewActions.actionOnItemAtPosition<>())
    }
}