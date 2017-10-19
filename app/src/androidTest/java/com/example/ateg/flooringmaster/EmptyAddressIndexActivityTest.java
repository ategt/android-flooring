package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class EmptyAddressIndexActivityTest {

    private final String TAG = "EmptyAddressIndexTest";

    private List<Address> resultsFromList;

    @Before
    public void setup() {
        resultsFromList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        if (resultsFromList != null) resultsFromList.clear();
        AddressDataListSingleton.clear();
    }

    @Rule
    public ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
            = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            AddressDao addressDao = mock(AddressDao.class);

            when(addressDao.list(any(ResultProperties.class))).thenReturn(new ArrayList<Address>());

            when(addressDao.size()).thenReturn(0);

            when(addressDao.get(anyInt())).thenReturn(null);

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItem_NotDisplayed() {
        List<Address> addresses = createList();

        int length = addresses.size();
        final Address lastAddress = addresses.get(length - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(findViewBasedOnTextView(lastAddress)).check(doesNotExist());
    }

    @NonNull
    private BaseMatcher<View> findViewBasedOnTextView(final Address firstAddress) {
        return new BaseMatcher<View>() {
            @Override
            public boolean matches(Object item) {

                if (item instanceof TextView) {
                    TextView textView = (TextView) item;
                    String viewText = textView.getText().toString();

                    if (!Strings.isNullOrEmpty(viewText) &&
                            viewText.toLowerCase().contains(firstAddress.getCompany().toLowerCase())) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        mainActivityActivityTestRule.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withId(R.id.address_list_empty))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));

    }

    @Test
    public void swipeRandomly() {
        Random random = new Random();
        mainActivityActivityTestRule.launchActivity(new Intent());

        ViewInteraction viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.address_index_listView));

        try {
            viewInteraction.check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
        } catch (junit.framework.AssertionFailedError ex){
            viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.address_list_empty));
            viewInteraction.check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
        }

        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                viewInteraction.perform(ViewActions.swipeUp());
            } else {
                viewInteraction.perform(ViewActions.swipeDown());
            }
        }

        Espresso.onView(ViewMatchers.withId(R.id.list_addresses_button_holding_relative_layout))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.create_addresss_action_button))));

        Espresso.onView(ViewMatchers.withId(R.id.create_addresss_action_button))
                .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
     }

    @NonNull
    private List<Address> createList() {
        final Address firstAddress = new Address();

        firstAddress.setCompany("Company A");
        firstAddress.setFirstName("Bill");
        firstAddress.setLastName("Billerston");
        firstAddress.setId(5);

        resultsFromList.add(firstAddress);

        return resultsFromList;
    }
}