package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.CursorMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.ateg.flooringmaster.AddressIndexActivityTest.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ATeg on 7/24/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddressUiMockTest {

    private List<Address> resultsFromList;

    @Before
    public void setup() {
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
            ResultProperties resultProperties1 = new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 1, 25);
            ResultProperties resultProperties2 = new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 2, 25);

            when(addressDao.list(resultProperties)).thenReturn(resultsFromList);
            when(addressDao.list(any(ResultProperties.class))).thenReturn(resultsFromList);

            when(addressDao.list(resultProperties1)).thenReturn(resultsFromList);
            when(addressDao.list(resultProperties2)).thenReturn(resultsFromList);
            when(addressDao.list()).thenReturn(resultsFromList);

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

    @Test
    public void simpleIndexWithOne() throws IOException {

        Address address = new Address();

        address.setCompany("Company A");
        address.setFirstName("Bill");
        address.setLastName("Billerston");

        resultsFromList.add(address);

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

    @Test
    public void simpleIndexWithLessThanTwenty() throws IOException {
        Address address = new Address();

        address.setCompany("Company A");
        address.setFirstName("Bill");
        address.setLastName("Billerston");

        resultsFromList.add(address);

        address = new Address();

        address.setCompany("Levis");
        address.setFirstName("Levi");
        address.setLastName("Pants");

        resultsFromList.add(address);

        address = new Address();

        address.setCompany("Company B");
        address.setFirstName("Bill");
        address.setLastName("Billerston");

        resultsFromList.add(address);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Address tempAddress = new Address();

            tempAddress.setCompany(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
            tempAddress.setFirstName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
            tempAddress.setLastName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));

            resultsFromList.add(tempAddress);
        }

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

    @Test
    public void simpleIndexWithMany() throws IOException {
        final Address firstAddress = new Address();

        firstAddress.setCompany("Company A");
        firstAddress.setFirstName("Bill");
        firstAddress.setLastName("Billerston");

        resultsFromList.add(firstAddress);

        final Address secondAddress = new Address();

        secondAddress.setCompany("Levis");
        secondAddress.setFirstName("Levi");
        secondAddress.setLastName("Pants");

        resultsFromList.add(secondAddress);

        final Address thirdAddress = new Address();

        thirdAddress.setCompany("Company B");
        thirdAddress.setFirstName("Bill");
        thirdAddress.setLastName("Billerston");

        resultsFromList.add(thirdAddress);

        Random random = new Random();

        for (int i = 0; i < 3000; i++) {
            Address tempAddress = new Address();

            tempAddress.setCompany(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
            tempAddress.setFirstName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
            tempAddress.setLastName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));

            resultsFromList.add(tempAddress);
        }

        final Address lastAddress = new Address();
        String lastCompanyName = "Company Final";
        lastAddress.setCompany(lastCompanyName);
        lastAddress.setFirstName("Last");
        lastAddress.setLastName("Person");

        resultsFromList.add(lastAddress);

        mainActivityActivityTestRule.launchActivity(new Intent());

        AddressDao addressDao = AddressDaoSingleton.getAddressDao(null);

        List<Address> addresses = addressDao.list(new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, 25));

        Assert.assertEquals(addresses.size(), 3004);

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

        DataInteraction thinger = Espresso.onData(getDataMatcher(secondAddress));


        ViewInteraction textView4 = onView(
                allOf(withId(R.id.address_list_item_nameTextView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        1),
                                0),
                        isDisplayed()));
        textView4.check(
                matches(
                        Matchers.anyOf(
                                withText(secondAddress.getLastName() + ", " + secondAddress.getFirstName()),
                                withText(secondAddress.getFirstName() + " " + secondAddress.getLastName()))
        ));



//        Espresso.onView(ViewMatchers.withId(R.id.address_index_listView))
//                .check()

        //Espresso.onData(ArgumentMatchers.anyList())
                //.

//        Espresso.onView(ViewMatchers.withId(R.id.address_index_listView))
//                .

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.address_index_listView),
                                withParent(withId(R.id.address_list_coordiator_layout))),
                        0),
                        isDisplayed()));
        //relativeLayout.


        ViewInteraction textView = onView(
                allOf(withId(R.id.address_show_fullName_textView),
                        ViewMatchers.withText(new org.hamcrest.Matcher() {
                            @Override
                            public void describeTo(Description description) {

                            }

                            @Override
                            public boolean matches(Object item) {

                                return true;
                            }

                            @Override
                            public void describeMismatch(Object item, Description mismatchDescription) {

                            }

                            @Override
                            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

                            }
                        }),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(
                ViewAssertions.matches(
                        ViewMatchers.withText("Bill Billerston")));



        DataInteraction firstRowInteraction = Espresso.onData(CursorMatchers.withRowString(new org.hamcrest.Matcher() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                return true;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }
        }, new org.hamcrest.Matcher<String>() {
            @Override
            public boolean matches(Object item) {
                return firstAddress.equals(item);
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        }));

        //ViewInteraction firstRowInteractionb = firstRowInteraction.perform(ViewActions.scrollTo());
        firstRowInteraction.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        DataInteraction thing = Espresso.onData(getDataMatcher(secondAddress));
                thing.perform(ViewActions.scrollTo());
                thing.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onData(getDataMatcher(firstAddress))
                .atPosition(1);

        Espresso.onData(getDataMatcher(secondAddress))
                .atPosition(2);

        Espresso.onData(getDataMatcher(secondAddress))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onData(getDataMatcher(secondAddress))
                .perform(ViewActions.scrollTo())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            //.check(ViewAssertions.matches(ViewMatchers.withText(org.hamcrest.Matcher<String>.)));

        Espresso.onData(getDataMatcher(thirdAddress))
                .atPosition(3004);

        Espresso.onData(getDataMatcher(lastAddress))
                .atPosition(3004);
        Espresso.onData(getDataMatcher(lastAddress))
                .check(ViewAssertions.matches(ViewMatchers.hasSibling(ViewMatchers.withId(R.id.address_list_item_nameTextView))));
        //.check(ViewAssertions.matches(ViewMatchers.));
        //.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onData(getDataMatcher(lastAddress)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onData(getDataMatcher(lastAddress)).check(ViewAssertions.matches(ViewMatchers.isClickable()));

    }

    @NonNull
    private BaseMatcher<Address> getDataMatcher(final Address lastAddress) {
        return new BaseMatcher<Address>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                return item.equals(lastAddress);
            }
        };
    }
}