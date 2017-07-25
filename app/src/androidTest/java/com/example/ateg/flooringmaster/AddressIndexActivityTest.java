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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddressIndexActivityTest {

    @Rule
    public ActivityTestRule<AddressIndexActivity> mActivityTestRule = new ActivityTestRule<>(AddressIndexActivity.class);

    @Test
    public void addressIndexActivityTest() {
        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.address_index_listView),
                                withParent(withId(R.id.address_list_coordiator_layout))),
                        10),
                        isDisplayed()));
        relativeLayout.perform(click());

        pressBack();

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.address_index_listView),
                                withParent(withId(R.id.address_list_coordiator_layout))),
                        24),
                        isDisplayed()));
        relativeLayout2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.address_show_fullName_textView), withText("D3FD4F75-B2F2-4949-9549-9C4C6BF957F0 C1C15CE6-05CD-4FC7-B402-2DF085BF37C8"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("D3FD4F75-B2F2-4949-9549-9C4C6BF957F0 C1C15CE6-05CD-4FC7-B402-2DF085BF37C8")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.address_show_id_textView), withText("ID:52313"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("ID:52313")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.address_show_id_textView), withText("ID:52313"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("ID:52313")));

        pressBack();

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("6eec2e7c-1020-4be3-82a6-db94c8b08166, 08e55442-8df1-4b60-ab41-6a2d05cc6bef"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        1),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("6eec2e7c-1020-4be3-82a6-db94c8b08166, 08e55442-8df1-4b60-ab41-6a2d05cc6bef")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.address_list_item_nameTextView), withText("6eec2e7c-1020-4be3-82a6-db94c8b08166, 08e55442-8df1-4b60-ab41-6a2d05cc6bef"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.address_index_listView),
                                        1),
                                0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.create_addresss_action_button),
                        childAtPosition(
                                allOf(withId(R.id.address_list_coordiator_layout),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                1),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.create_addresss_action_button),
                        childAtPosition(
                                allOf(withId(R.id.address_list_coordiator_layout),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                1),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

    }

    public static Matcher<View> childAtPosition(
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
