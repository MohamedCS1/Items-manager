package com.example.p_scanner


import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class AddAndEditItemActivityTest:TestCase(){

    lateinit var scenario:ActivityScenario<AddAndEditItemActivity>

    @Before
    fun before(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testView(){
        onView(withId(R.id.et_id)).perform(ViewActions.typeText("3544356"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.et_title)).perform(ViewActions.typeText("Any Title"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.et_price)).perform(ViewActions.typeText("56000"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.et_description)).perform(ViewActions.typeText("Any Description"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.bu_add_or_edit_item)).perform(click())
    }

}