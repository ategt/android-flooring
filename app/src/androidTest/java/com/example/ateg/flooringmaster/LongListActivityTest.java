package com.example.ateg.flooringmaster;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LongListActivityTest {

    private static final String TEXT_ITEM_30 = "item: 30";

    private static final String TEXT_ITEM_30_SELECTED = "30";

    private static final String TEXT_ITEM_60 = "item: 60";

    // Match the last item by matching its text.
    private static final String LAST_ITEM_ID = "item: 99";

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

            when(addressDao.list(any(ResultProperties.class))).thenReturn(resultsFromList);

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };


    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        //onView(withText(LAST_ITEM_ID)).check(doesNotExist());

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

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(new BaseMatcher<View>() {
            @Override
            public boolean matches(Object item) {

                if (item instanceof TextView){
                    TextView textView = (TextView) item;
                    String viewText = textView.getText().toString();

                    if (!Strings.isNullOrEmpty(viewText) && viewText.toLowerCase().contains(lastCompanyName.toLowerCase())){
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

//    /**
//     * Check that the item is created. onData() takes care of scrolling.
//     */
//    @Test
//    public void list_Scrolls() {
//        onRow(LAST_ITEM_ID).check(matches(isCompletelyDisplayed()));
//    }
//
//    /**
//     * Clicks on a row and checks that the activity detected the click.
//     */
//    @Test
//    public void row_Click() {
//        // Click on one of the rows.
//        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());
//
//        // Check that the activity detected the click on the first column.
//        onView(ViewMatchers.withId(R.id.selection_row_value))
//                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
//    }
//
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
//
//    /**
//     * Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
//     * <p>
//     * Note: A custom matcher can be used to match the content and have more readable code.
//     * See the Custom Matcher Sample.
//     * </p>
//     *
//     * @param str the content of the field
//     * @return a {@link DataInteraction} referencing the row
//     */
//    private static DataInteraction onRow(String str) {
//        return onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is(str)));
//    }

//    private static DataInteraction onRow(String str) {
//        return onData(hasEntry(equalTo(A.ROW_TEXT), is(str)));
//    }
}