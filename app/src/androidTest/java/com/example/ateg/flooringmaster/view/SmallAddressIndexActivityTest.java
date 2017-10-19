package com.example.ateg.flooringmaster.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
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
import com.example.ateg.flooringmaster.R;
import com.example.ateg.flooringmaster.ResultProperties;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
public class SmallAddressIndexActivityTest {

    private final String TAG = "SmallAddressIndexTest";
    private final int smallCountAddressItems = 16;

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

            when(addressDao.list(argThat(evalResultPropertiesPageNumber(0)))).thenReturn(resultsFromList);

            when(addressDao.list(argThat(evalResultPropertiesPageNumberNotInArray(0, 1, 2))))
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
    private ArgumentMatcher<ResultProperties> evalResultPropertiesPageNumberNotInArray(final int... pagesExpected) {
        return new ArgumentMatcher<ResultProperties>() {
            @Override
            public boolean matches(ResultProperties argument) {
                if (argument == null)
                    return false;
                if (argument instanceof ResultProperties) {
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
    private ArgumentMatcher<ResultProperties> evalResultPropertiesPageNumber(final int pageNumber) {
        return new ArgumentMatcher<ResultProperties>() {
            @Override
            public boolean matches(ResultProperties argument) {
                if (argument == null)
                    return false;
                if (argument instanceof ResultProperties) {
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
    public void lastItem_NotDisplayed() {
        List<Address> addresses = createList(smallCountAddressItems);

        int length = addresses.size();
        final Address lastAddress = addresses.get(length - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(findViewBasedOnTextView(lastAddress)).check(doesNotExist());
    }

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void firstItemNotDisplayedAtBottom() {
        List<Address> addresses = createList(smallCountAddressItems);

        final Address firstAddress = addresses.get(0);

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onView(findViewBasedOnTextView(firstAddress)).check(doesNotExist());
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
        List<Address> addresses = createList(smallCountAddressItems);

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
        List<Address> addresses = createList(smallCountAddressItems);

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
                .check(ViewAssertions.matches(ViewMatchers.withId(R.id.address_index_listView)))
                .check(ViewAssertions.matches(ViewMatchers.hasSibling(ViewMatchers.withId(R.id.create_addresss_action_button))));

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
        List<Address> addresses = createList(smallCountAddressItems);
        int sizeOfList = resultsFromList.size();
        Address lastAddress = resultsFromList.get(sizeOfList - 1);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onRow(lastAddress).check(matches(isCompletelyDisplayed()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void clickOnFirstItem() {
        List<Address> addresses = createList(smallCountAddressItems);
        Address firstAddress = resultsFromList.get(0);

        final String firstAddressFullName = firstAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(firstAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddressFullName)));

        mainActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddressFullName)));

        mainActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(firstAddressFullName)));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void clickOnLastItem() {
        List<Address> addresses = createList(smallCountAddressItems);
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
    public void clickOnRandomItemThenRotate() {
        List<Address> addresses = createList(smallCountAddressItems);
        int sizeOfList = resultsFromList.size();
        int addressPageIndex = new Random().nextInt(sizeOfList - 1);
        Address randomAddress = resultsFromList.get(addressPageIndex);

        final String randomAddressFullName = randomAddress.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        onRow(randomAddress).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(randomAddressFullName)));
        mainActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(randomAddressFullName)));

        mainActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
    private List<Address> createList(int addressesToGenerate) {
        final Address firstAddress = new Address();

        firstAddress.setCompany("Company A");
        firstAddress.setFirstName("Bill");
        firstAddress.setLastName("Billerston");
        firstAddress.setId(3856);

        resultsFromList.add(firstAddress);

        final Address secondAddress = new Address();

        secondAddress.setCompany("Levis");
        secondAddress.setFirstName("Levi");
        secondAddress.setLastName("Pants");
        secondAddress.setId(7800);

        resultsFromList.add(secondAddress);

        final Address thirdAddress = new Address();

        thirdAddress.setCompany("Company B");
        thirdAddress.setFirstName("Bill");
        thirdAddress.setLastName("Billerston");
        thirdAddress.setId(4800);

        resultsFromList.add(thirdAddress);

        Random random = new Random();

        for (int i = 0; i < addressesToGenerate; i++) {
            Address tempAddress = generateRandomDisplayAddress(random, i);

            resultsFromList.add(tempAddress);
        }

        final Address lastAddress = new Address();
        final String lastCompanyName = "Company Final";
        lastAddress.setCompany(lastCompanyName);
        lastAddress.setFirstName("Last");
        lastAddress.setLastName("Person");
        lastAddress.setId(3577);

        resultsFromList.add(lastAddress);
        return resultsFromList;
    }

    @NonNull
    private Address generateRandomDisplayAddress(Random random, int i) {
        Address tempAddress = new Address();

        String uuid = UUID.randomUUID().toString();
        int length = uuid.length();
        int randInt = random.nextInt(length);
        String company = uuid.substring(0, randInt);

        tempAddress.setCompany(company);
        tempAddress.setId(i);
        tempAddress.setFirstName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
        tempAddress.setLastName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
        return tempAddress;
    }

    private Address getAddressById(Integer value) {
        Address foundAddress = null;
        for (Address address : resultsFromList) {
            Log.w(TAG, "Value: " + value + ", " + (address == null ? " -- " : address.getFullName()));
            if (value != null && address != null && Integer.compare(address.getId(), value) == 0) {
                foundAddress = address;
                break;
            }
        }

        if (foundAddress == null) {
            Log.e(TAG, "Integer Not Found: " + value);
        } else {
            Log.i(TAG, "Address Found: " + foundAddress.getId() + " - " + foundAddress.getFullName());
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
                if (anAddressIsTrue[0]) {
                    anAddressIsTrue[0] = false;
                    return true;
                }

                Address address = (Address) item;
                lastAddress[0] = address;
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