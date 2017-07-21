package com.example.ateg.flooringmaster;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;

import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddressUiTest {

    @Rule
    public ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
            = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class);

    @Test
    public void examineEmptyIndexTest() {

        Espresso.onView(ViewMatchers.withId(R.id.address_list_coordiator_layout))
                .check(ViewAssertions.matches(
                        ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.create_addresss_action_button))
                ));

        Espresso.onView(ViewMatchers.withId(R.id.create_addresss_action_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.address_list_coordiator_layout))
                .check(ViewAssertions.matches(
                        ViewMatchers.hasDescendant(
                                ViewMatchers.withId(R.id.address_index_listView)
                        )
                ));

    }

    @Test
    public void simpleIndex() {
        Espresso.onView(ViewMatchers.withId(R.id.address_index_listView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .check(ViewAssertions.matches(ViewMatchers.withId(R.id.address_index_listView)))
                .check(ViewAssertions.matches(ViewMatchers.hasSibling(ViewMatchers.withId(R.id.create_addresss_action_button))));
    }
}