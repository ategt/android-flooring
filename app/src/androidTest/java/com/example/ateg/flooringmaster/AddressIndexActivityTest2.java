package com.example.ateg.flooringmaster;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddressIndexActivityTest2 {

    @Rule
    public ActivityTestRule<AddressIndexActivity> mActivityTestRule = new ActivityTestRule<>(AddressIndexActivity.class);

    @Test
    public void addressIndexActivityTest2() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("Billerston, Bill"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Billerston, Bill")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Company A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Company A")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("Pants, Levi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Pants, Levi")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Levis"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        1),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Levis")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("Billerston, Bill"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        2),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Billerston, Bill")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Company B"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        2),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("Company B")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Company B"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        2),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("Company B")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("Person, Last"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        5),
                                0),
                        isDisplayed()));
        textView8.check(matches(withText("Person, Last")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Company Final"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        5),
                                1),
                        isDisplayed()));
        textView9.check(matches(withText("Company Final")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.address_list_item_companyTextView), withText("Company Final"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        5),
                                1),
                        isDisplayed()));
        textView10.check(matches(withText("Company Final")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
