package com.example.ateg.flooringmaster.view;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ateg.flooringmaster.AddressIndexActivity;
import com.example.ateg.flooringmaster.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
                .check(ViewAssertions.matches(ViewMatchers.hasSibling(ViewMatchers.withId(R.id.list_addresses_button_holding_relative_layout))));

        Espresso.onView(ViewMatchers.withId(R.id.list_addresses_button_holding_relative_layout))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.create_addresss_action_button))));

        Espresso.onView(ViewMatchers.withId(R.id.create_addresss_action_button))
                .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }

    @Test
    public void simpleIndexEmpty() {

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
                .check(ViewAssertions.matches(ViewMatchers.hasSibling(ViewMatchers.withId(R.id.list_addresses_button_holding_relative_layout))));

        Espresso.onView(ViewMatchers.withId(R.id.list_addresses_button_holding_relative_layout))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.create_addresss_action_button))));

        Espresso.onView(ViewMatchers.withId(R.id.create_addresss_action_button))
                .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }
}