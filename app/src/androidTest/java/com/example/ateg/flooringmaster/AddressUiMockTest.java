package com.example.ateg.flooringmaster;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

/**
 * Created by ATeg on 7/21/2017.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest(com.example.ateg.flooringmaster.AddressBufferedClient.class)
public class AddressUiMockTest {

    @Test
    public void examineEmptyIndexTest2() throws Exception {
        ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
                = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class);

        AddressBufferedClient addressBufferedClient = PowerMockito.mock(AddressBufferedClient.class);
        PowerMockito.when(AddressBufferedClient.class, "list", Matchers.any(ResultProperties.class))
                .thenReturn(new ArrayList<Address>());

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
