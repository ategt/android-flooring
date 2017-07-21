package com.example.ateg.flooringmaster;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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



    }

//        Espresso.onView(ViewMatchers.withId(R.id.action_button)).perform(click());
//
//        Espresso.onView(ViewMatchers.withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));
//
//    }

    @Test
    public void simpleIndex(){

    }
}
