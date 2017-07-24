package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ATeg on 7/24/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddressUiMockTest {

    @Rule
    public ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
            = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            AddressDao addressDao = mock(AddressDao.class);

            ResultProperties resultProperties = new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, 25);

            when(addressDao.list(resultProperties)).thenReturn(new ArrayList<Address>());
            when(addressDao.list()).thenReturn(new ArrayList<Address>());

            AddressDaoSingleton.setAddressDao(addressDao);
        }
    };


    @Test
    public void simpleIndexEmpty() throws IOException {

        //mainActivityActivityTestRule.

        //Context c2 = InstrumentationRegistry.getTargetContext();
        //Intent i2 = new Intent(c2, AddressIndexActivity.class);
        //c2.startActivity(i2);

        //c2.start

        //new android.support.test.espresso.Espresso().

        //i2.get

//        ActivityTestRule<AddressIndexActivity> mainActivityActivityTestRule
//                = new ActivityTestRule<AddressIndexActivity>(AddressIndexActivity.class);
//
//        Activity activity = mainActivityActivityTestRule.getActivity();
//
//        Context context = InstrumentationRegistry.getTargetContext();
//
//        Intent intent = mainActivityActivityTestRule.getActivity().getIntent();
//        activity.startActivity(intent);

        //mainActivityActivityTestRule.

        //activity.
        //context.startActivity(activity);
        //new Instrumentation().s;

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

        //AddressIndexView addressIndexView = mock(AddressIndexView.class);

        //AddressClient addressClient = new AddressClientMockup();

        //AddressIndexPresenter indexPresenter = new AddressIndexPresenter(addressIndexView, addressClient);

        //indexPresenter.attachView(addressIndexView);

//        indexPresenter.loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, 25));
//
//        indexPresenter.loadNextPage();
//        indexPresenter.loadNextPage();
//        indexPresenter.loadNextPage();
//        indexPresenter.loadNextPage();
//        indexPresenter.loadNextPage();
//        indexPresenter.loadNextPage();

    }

//    private class AddressClientMockup implements AddressClient {
//
//        @Override
//        public Address create(Address address) {
//            return null;
//        }
//
//        @Override
//        public void update(Address address) {
//
//        }
//
//        @Override
//        public Address get(Integer id) {
//            return null;
//        }
//
//        @Override
//        public Address get(String input) {
//            return null;
//        }
//
//        @Override
//        public Address delete(Integer id) {
//            return null;
//        }
//
//        @Override
//        public int size() {
//            return 0;
//        }
//
//        @Override
//        public Set<String> getCompletionGuesses(String input, int limit) {
//            return null;
//        }
//
//        @Override
//        public List<Address> list(ResultProperties resultProperties) {
//            return new ArrayList();
//        }
//
//        @Override
//        public List<Address> search(AddressSearchRequest addressSearchRequest, ResultProperties resultProperties) {
//            return null;
//        }
//    }

}