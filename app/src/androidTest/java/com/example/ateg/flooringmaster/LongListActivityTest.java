package com.example.ateg.flooringmaster;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
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
@LargeTest
public class LongListActivityTest {

    private final String TAG = "LongListActivityTest";

    private List<Address> resultsFromList;
    private List<Address> resultsFromListPage1;
    private List<Address> resultsFromListPage2;
    private List<Address> addressesExpected;

    @Before
    public void setup() {
        resultsFromList = new ArrayList<>();
        addressesExpected = new ArrayList<>();

        Random random = new Random();
        resultsFromListPage1 = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            resultsFromListPage1.add(generateRandomDisplayAddress(random, i + 3500));
        }

        resultsFromListPage2 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            resultsFromListPage2.add(generateRandomDisplayAddress(random, i + 4000));
        }
    }

    @After
    public void tearDown() {
        if (resultsFromList != null) resultsFromList.clear();
        if (resultsFromListPage1 != null) resultsFromListPage1.clear();
        if (resultsFromListPage2 != null) resultsFromListPage2.clear();
        if (addressesExpected != null) addressesExpected.clear();
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
            when(addressDao.list(argThat(evalResultPropertiesPageNumber(1)))).thenReturn(resultsFromListPage1);
            when(addressDao.list(argThat(evalResultPropertiesPageNumber(2)))).thenReturn(resultsFromListPage2);

            when(addressDao.list(argThat(evalResultPropertiesPageNumberNotInArray(0, 1, 2))))
                    .thenReturn(new ArrayList<Address>());

            when(addressDao.size()).thenReturn(resultsFromList.size()
                    + resultsFromListPage1.size() + resultsFromListPage2.size());

            ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
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
    public void row_Click_From_First_Page() {
        List<Address> addresses = createList(3000);
        int sizeOfList = resultsFromListPage1.size();
        int addressPageIndex = new Random().nextInt(sizeOfList - 1);
        Address addressFromFirstPage = resultsFromListPage1.get(addressPageIndex);

        final String addressFromFirstPageFullName = addressFromFirstPage.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        scrollToLastRow();

        loadExpectedAddresses(addressFromFirstPage);

        onRow(addressFromFirstPage).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(addressFromFirstPageFullName)));
    }

    private void loadExpectedAddresses(Address addressFromFirstPage) {
        int addressIndex = AddressDataListSingleton.indexOf(addressFromFirstPage);

        addressesExpected.add(addressFromFirstPage);
        Log.i(TAG, "Source: " + (addressFromFirstPage == null ? "null" : addressFromFirstPage.getId()));
        Log.i(TAG, "DataIndex: " + addressIndex);

        Address sourceAddress = AddressDataListSingleton.getOrNull(addressIndex);
        Log.i(TAG, "SourceAddressData: " + (sourceAddress == null ? "null" : sourceAddress.getId()));

        Address previousAddress = AddressDataListSingleton.getOrNull(addressIndex - 1);
        addressesExpected.add(previousAddress);
        Log.i(TAG, "previousAddress: " + (previousAddress == null ? "null" : previousAddress.getId()));

        Address previousAddress2 = AddressDataListSingleton.getOrNull(addressIndex - 2);
        addressesExpected.add(previousAddress2);
        Log.i(TAG, "previousAddress2: " + (previousAddress2 == null ? "null" : previousAddress2.getId()));

        Address nextAddress = AddressDataListSingleton.getOrNull(addressIndex + 1);
        addressesExpected.add(nextAddress);
        Log.i(TAG, "nextAddress: " + (nextAddress == null ? "null" : nextAddress.getId()));

        Address nextAddress2 = AddressDataListSingleton.getOrNull(addressIndex + 2);
        addressesExpected.add(nextAddress2);
        Log.i(TAG, "nextAddress2: " + (nextAddress2 == null ? "null" : nextAddress2.getId()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click_From_Second_Page() {
        List<Address> addresses = createList(3000);
        int sizeOfList = resultsFromListPage2.size();
        Address addressFromSecondPage = resultsFromListPage2.get(new Random().nextInt(sizeOfList - 1));
        addressesExpected.add(addressFromSecondPage);

        final String addressFromSecondPageFullName = addressFromSecondPage.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        // Click on one of the rows.
        onRow(addressFromSecondPage).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(addressFromSecondPageFullName)));
    }


    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click_Last_Item_From_Last_Page() {
        List<Address> addresses = createList(3000);
        int sizeOfList = resultsFromListPage2.size();
        Address lastAddressFromSecondPage = resultsFromListPage2.get(sizeOfList - 1);
        addressesExpected.add(lastAddressFromSecondPage);

        final String lastAddressFromSecondPageFullName = lastAddressFromSecondPage.getFullName();

        mainActivityActivityTestRule.launchActivity(new Intent());

        // Click on one of the rows.
        onRow(lastAddressFromSecondPage).onChildView(withId(R.id.address_list_item_nameTextView)).perform(click());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.address_show_fullName_textView),
                ViewMatchers.isDisplayed()))
                .check(matches(isShowingInputAddress(lastAddressFromSecondPageFullName)));
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
            Address tempAddress = generateRandomDisplayAddress(random, i);

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

    @NonNull
    private Address generateRandomDisplayAddress(Random random, int i) {
        Address tempAddress = new Address();

        String uuid = UUID.randomUUID().toString();
        int length = uuid.length();
        int randInt = random.nextInt(length);
        Log.i(TAG, uuid + ", Length:" + length + ", RandomInt:" + randInt);
        String company = uuid.substring(0, randInt);
        Log.i(TAG, i + " : " + company);

        tempAddress.setCompany(company);
        tempAddress.setId(i);
        tempAddress.setFirstName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
        tempAddress.setLastName(UUID.randomUUID().toString().substring(0, random.nextInt(20)));
        return tempAddress;
    }

    private Address getAddressById(Integer value) {
        Address foundAddress = null;
        for (Address address : addressesExpected) {
            if (address != null && Integer.compare(address.getId(), value) == 0) {
                foundAddress = address;
                break;
            }
        }

        if (foundAddress == null) {
            Log.e(TAG, "Integer Not Found: " + value);

            for (Address address : resultsFromList) {
                if (address.getId() == value) {
                    foundAddress = address;
                    break;
                }
            }
        }

        if (foundAddress == null) {
            for (Address address : resultsFromListPage1) {
                if (address.getId() == value) {
                    foundAddress = address;
                    break;
                }
            }
        }

        if (foundAddress == null) {
            for (Address address : resultsFromListPage2) {
                if (address.getId() == value) {
                    foundAddress = address;
                    break;
                }
            }
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
}