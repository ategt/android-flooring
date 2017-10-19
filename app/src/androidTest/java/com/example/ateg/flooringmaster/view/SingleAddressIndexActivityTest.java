package com.example.ateg.flooringmaster.view;

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

import com.example.ateg.flooringmaster.Address;
import com.example.ateg.flooringmaster.AddressDao;
import com.example.ateg.flooringmaster.AddressDaoSingleton;
import com.example.ateg.flooringmaster.AddressDataListSingleton;
import com.example.ateg.flooringmaster.AddressIndexActivity;
import com.example.ateg.flooringmaster.AddressSortByEnum;
import com.example.ateg.flooringmaster.R;
import com.example.ateg.flooringmaster.ResultSegment;

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
import java.util.UUID;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class SingleAddressIndexActivityTest {

    private final String TAG = "SingleAddressIndexTest";

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

            when(addressDao.list(argThat(evalResultSegmentPageNumber(0)))).thenReturn(resultsFromList);

            when(addressDao.list(argThat(evalResultSegmentPageNumberNotInArray(0))))
                    .thenReturn(new ArrayList<Address>());

            when(addressDao.size()).thenReturn(resultsFromList.size());

            when(addressDao.get(anyInt())).thenAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    Object objArg = invocation.getArgument(0);
                    Integer arg = (int) objArg;
                    return getAddressById(arg);
                }
            });

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };

    @NonNull
    private ArgumentMatcher<ResultSegment<AddressSortByEnum>> evalResultSegmentPageNumberNotInArray(final int... pagesExpected) {
        return new ArgumentMatcher<ResultSegment<AddressSortByEnum>>() {
            @Override
            public boolean matches(ResultSegment<AddressSortByEnum> argument) {
                if (argument == null)
                    return false;
                if (argument instanceof ResultSegment) {
                    for (int i : pagesExpected) {
                        if (argument.getPageNumber() == i) {
                            return false;
                        }
                    }
                    return argument.getPageNumber() != 0;
                }
                return false;
            }
        };
    }

    @NonNull
    private ArgumentMatcher<ResultSegment<AddressSortByEnum>> evalResultSegmentPageNumber(final int pageNumber) {
        return new ArgumentMatcher<ResultSegment<AddressSortByEnum>>() {
            @Override
            public boolean matches(ResultSegment<AddressSortByEnum> argument) {
                if (argument == null)
                    return false;
                if (argument instanceof ResultSegment) {
                    return argument.getPageNumber() == pageNumber;
                }
                return false;
            }
        };
    }

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItemDisplayed() {
        List<Address> addresses = createList();

        int length = addresses.size();
        final Address lastAddress = addresses.get(length - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(findViewBasedOnTextView(lastAddress)).check(matches(isCompletelyDisplayed()));
    }

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void firstItemDisplayedAtBottom() {
        List<Address> addresses = createList();

        final Address firstAddress = addresses.get(0);

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onView(findViewBasedOnTextView(firstAddress)).check(matches(isCompletelyDisplayed()));
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
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void firstItemDisplayedAndClickable() throws InterruptedException {
        List<Address> addresses = createList();

        final Address firstAddress = addresses.get(0);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onRow(firstAddress).check(matches(isCompletelyDisplayed()));

        onRow(firstAddress).onChildView(ViewMatchers.withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddress.getFullName())));
    }

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void firstItemDisplayedAfterRoundTrip() throws InterruptedException {
        List<Address> addresses = createList();

        final Address lastAddress = resultsFromList.get(resultsFromList.size() - 1);
        final Address firstAddress = addresses.get(0);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onRow(firstAddress).check(matches(isCompletelyDisplayed()));

        scrollToLastRow();
        scrollToFirstRow();

        onRow(firstAddress).check(matches(isCompletelyDisplayed()));

        Thread.sleep(50);
        scrollToLastRow();
        scrollToFirstRow();

        onRow(firstAddress).check(matches(isCompletelyDisplayed()));

        Thread.sleep(50);
        scrollToLastRow();

        onRow(lastAddress).check(matches(isCompletelyDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.address_index_listView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())
                .check(ViewAssertions.matches(ViewMatchers.withId(R.id.address_index_listView)));

        Espresso.onView(withId(R.id.create_addresss_action_button))
                .check(matches(isCompletelyDisplayed()));

        scrollToFirstRow();

        onRow(firstAddress).check(matches(isCompletelyDisplayed()));

        onRow(firstAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddress.getFullName())));
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        List<Address> addresses = createList();
        int sizeOfList = resultsFromList.size();
        Address lastAddress = resultsFromList.get(sizeOfList - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onRow(lastAddress).check(matches(isCompletelyDisplayed()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click_From_First_Page() {
        List<Address> addresses = createList();
        int sizeOfList = resultsFromList.size();

        int addressPageIndex = new Random().nextInt(sizeOfList);
        Address addressFromFirstPage = resultsFromList.get(addressPageIndex);

        final String addressFromFirstPageFullName = addressFromFirstPage.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(addressFromFirstPage).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(addressFromFirstPageFullName)));
    }


    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void clickOnFirstItem() {
        List<Address> addresses = createList();
        Address firstAddress = resultsFromList.get(0);

        final String firstAddressFullName = firstAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(firstAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddressFullName)));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void clickOnLastItem() {
        List<Address> addresses = createList();
        int sizeOfList = resultsFromList.size();
        Address lastAddress = resultsFromList.get(sizeOfList - 1);

        final String lastAddressFullName = lastAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(lastAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(lastAddressFullName)));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void clickOnRandomItem() {
        List<Address> addresses = createList();
        int sizeOfList = resultsFromList.size();
        int addressPageIndex = new Random().nextInt(sizeOfList);
        Address randomAddress = resultsFromList.get(addressPageIndex);

        final String randomAddressFullName = randomAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(randomAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(randomAddressFullName)));
    }


    @NonNull
    private BaseMatcher<View> isShowingInputAddress(final String lastAddressFromSecondPageFullName) {
        return new BaseMatcher<View>() {
            @Override
            public boolean matches(Object item) {
                if (item instanceof TextView) {
                    TextView textView = (TextView) item;
                    String viewText = textView.getText().toString();

                    if (!Strings.isNullOrEmpty(viewText) && viewText.equalsIgnoreCase(lastAddressFromSecondPageFullName)) {
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

    private Address getAddressById(Integer value) {
        Address foundAddress = null;
        for (Address address : resultsFromList) {
            if (value != null && address != null && Integer.compare(address.getId(), value) == 0) {
                foundAddress = address;
                break;
            }
        }

        if (foundAddress == null) {
            Log.e(TAG, "Integer Not Found: " + value);
        }

        return foundAddress;
    }

    private static DataInteraction onRow(final Address addressToFind) {
        return onData(new BaseMatcher() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                if (item instanceof Address) {
                    Address address = (Address) item;
                    return Objects.equals(address, addressToFind);
                }

                return false;
            }
        });
    }

    private static ViewInteraction scrollToLastRow() {

        final Address[] lastAddress = scrollToFirstRow();

        final Address finalAddress = lastAddress[0];

        return onData(new BaseMatcher() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                if (item instanceof Address) {
                    Address address = (Address) item;
                    return Objects.equals(address, finalAddress);
                }

                return false;
            }
        }).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                int id = view.getId();
            }
        });
    }

    @NonNull
    private static Address[] scrollToFirstRow() {
        final Address[] lastAddress = new Address[1];
        final boolean[] anAddressIsTrue = new boolean[1];
        anAddressIsTrue[0] = true;

        onData(new BaseMatcher() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                Address address = (Address) item;
                lastAddress[0] = address;

                if (anAddressIsTrue[0]) {
                    anAddressIsTrue[0] = false;
                    return true;
                }
                return false;
            }
        }).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                int id = view.getId();
            }
        });
        return lastAddress;
    }
}