package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ATeg on 7/24/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddressUiMockTest {

    private List<Address> resultsFromList;

    @Before
    public void setup(){
        resultsFromList = new ArrayList<>();
    }

    @Rule
    public ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
            = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            AddressDao addressDao = mock(AddressDao.class);

            ResultProperties resultProperties = new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, 25);

            when(addressDao.list(resultProperties)).then(new Answer<List<Address>>() {
                @Override
                public List<Address> answer(InvocationOnMock invocation) throws Throwable {
                    Thread.sleep(500);
                    return resultsFromList;
                }
            });

            when(addressDao.list()).then(new Answer<List<Address>>() {
                @Override
                public List<Address> answer(InvocationOnMock invocation) throws Throwable {
                    Thread.sleep(500);
                    return resultsFromList;
                }
            });

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };

    @Test
    public void simpleIndexEmpty() throws IOException {

        mainActivityActivityTestRule.launchActivity(new Intent());

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