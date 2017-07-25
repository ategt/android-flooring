package com.example.ateg.flooringmaster;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LongListActivityTest {

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
            //when(addressDao.list(any(ResultProperties.class))).thenReturn(resultsFromList);

            when(addressDao.list(argThat(new ArgumentMatcher<ResultProperties>() {
                @Override
                public boolean matches(ResultProperties argument) {
                    if (argument == null)
                        return false;
                    if (argument instanceof ResultProperties) {
                        return argument.getPageNumber() == 0;
                    }
                    return false;
                }
            }))).thenReturn(resultsFromList);

            when(addressDao.list(argThat(new ArgumentMatcher<ResultProperties>() {
                @Override
                public boolean matches(ResultProperties argument) {
                    if (argument == null)
                        return false;
                    if (argument instanceof ResultProperties) {
                        return argument.getPageNumber() != 0;
                    }
                    return false;
                }
            }))).thenReturn(new ArrayList<Address>());
            when(addressDao.size()).thenReturn(resultsFromList.size());

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItem_NotDisplayed() {
        List<Address> addresses = createList(3000);

        int length = addresses.size();
        final Address lastAddress = addresses.get(length - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(new BaseMatcher<View>() {
            @Override
            public boolean matches(Object item) {

                if (item instanceof TextView) {
                    TextView textView = (TextView) item;
                    String viewText = textView.getText().toString();

                    if (!Strings.isNullOrEmpty(viewText) &&
                            viewText.toLowerCase().contains(lastAddress.getCompany().toLowerCase())) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {

            }
        }).check(doesNotExist());
    }

    @NonNull
    private List<Address> createList(int addressesToGenerate) {
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

        for (int i = 0; i < addressesToGenerate; i++) {
            Address tempAddress = new Address();

            String uuid = UUID.randomUUID().toString();
            int length = uuid.length();
            int randInt = random.nextInt(length);
            Log.i("asd", uuid + ", Length:" + length + ", RandomInt:" + randInt);
            String company = uuid.substring(0, randInt);
            Log.i("adf", i + " : " + company);

            tempAddress.setCompany(company);
            tempAddress.setFirstName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
            tempAddress.setLastName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));

            resultsFromList.add(tempAddress);
        }

        final Address lastAddress = new Address();
        final String lastCompanyName = "Company Final";
        lastAddress.setCompany(lastCompanyName);
        lastAddress.setFirstName("Last");
        lastAddress.setLastName("Person");

        resultsFromList.add(lastAddress);
        return resultsFromList;
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        List<Address> addresses = createList(3000);
        int sizeOfList = resultsFromList.size();
        Address lastAddress = resultsFromList.get(sizeOfList - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onRow(lastAddress).check(matches(isCompletelyDisplayed()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click() {
        List<Address> addresses = createList(3000);
        int sizeOfList = resultsFromList.size();
        Address lastAddress = resultsFromList.get(sizeOfList - 1);

        final String lastAddressFullName = lastAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        // Click on one of the rows.
        DataInteraction di1 = onRow(lastAddress);
        DataInteraction di2 = di1.onChildView(withId(R.id.address_list_item_nameTextView));
        ViewInteraction vi3 = di2.perform(click());

        // Check that the activity detected the click on the first column.
        //onView(ViewMatchers.withId(R.id.address_show_fullName_textView))
        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {

                        if (item instanceof TextView) {
                            TextView textView = (TextView) item;
                            String viewText = textView.getText().toString();

                            if (!Strings.isNullOrEmpty(viewText) && viewText.equalsIgnoreCase(lastAddressFullName)) {
                                return true;
                            }

                        }

                        return false;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }));
    }

//    /**
//     * Checks that a toggle button is checked after clicking on it.
//     */
//    @Test
//    public void toggle_Click() {
//        // Click on a toggle button.
//        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).perform(click());
//
//        // Check that the toggle button is checked.
//        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).check(matches(isChecked()));
//    }
//
//    /**
//     * Make sure that clicking on the toggle button doesn't trigger a click on the row.
//     */
//    @Test
//    public void toggle_ClickDoesntPropagate() {
//        // Click on one of the rows.
//        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());
//
//        // Click on the toggle button, in a different row.
//        onRow(TEXT_ITEM_60).onChildView(withId(R.id.rowToggleButton)).perform(click());
//
//        // Check that the activity didn't detect the click on the first column.
//        onView(ViewMatchers.withId(R.id.selection_row_value))
//                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
//    }

    private static DataInteraction onRow(final Address addressToFind) {
        return onData(new BaseMatcher() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object item) {
                if (item instanceof Address) {
                    Address address = (Address) item;
                    boolean isSame = Objects.equals(address, addressToFind);
                    if (isSame) {
                        return true;
                    }
                }

                return false;
            }
        });
    }
}