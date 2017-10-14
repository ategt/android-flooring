package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.UUID;

import static com.example.ateg.flooringmaster.AddressTest.addressBuilder;
import static com.example.ateg.flooringmaster.AddressTest.addressCloner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ATeg on 10/13/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddressClientSqliteImplTest {

    AddressDao addressDao;
    Context appContext;

    @Before
    public void setUp() throws Exception {
        Activity activity = Robolectric.setupActivity(MockActivity.class);

        //appContext = InstrumentationRegistry.getTargetContext();
        appContext = activity;
        addressDao = new AddressClientSqliteImpl(appContext, "Test-Flooring-DB", 2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void indexTest() {
        List<Address> addressList = addressDao.list(null);

        addressList.size();
        assertNotNull(addressList);
    }

    @Test
    public void testCRUD() throws InterruptedException {
        System.out.println("CRUD test");

        String city = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String zip = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String streetNumber = UUID.randomUUID().toString();
        String streetName = UUID.randomUUID().toString();

        Address address = addressBuilder(city, company, firstName, lastName, state, streetName, streetNumber, zip);

        int beforeCreation = addressDao.size(true);

        Address result = addressCloner(addressDao.create(address));
        int afterCreation = addressDao.size(true);

        assertEquals(beforeCreation + 1, afterCreation);

        assertNotNull(result);
        assertNotNull(result.getId());

        assertTrue(result.getId() > 0);

        Gson gson = new GsonBuilder().create();

        Address retrivedAddress = addressDao.get(result.getId());
        assertEquals("Addresses not equal:\n" +
                "\tFrom Db:     " + gson.toJson(retrivedAddress) +
                "\tFrom Create: " + gson.toJson(result), retrivedAddress, result);

        retrivedAddress.setCity(UUID.randomUUID().toString());

        addressDao.update(retrivedAddress);

        Address companyAddress = addressDao.getByCompany(retrivedAddress.getCompany());

        assertEquals(companyAddress, retrivedAddress);
        assertNotEquals(result, companyAddress);

        assertEquals(afterCreation, addressDao.size());

        addressDao.delete(companyAddress.getId());

        assertEquals(afterCreation - 1, addressDao.size(true));

        Address deletedAddress = addressDao.get(companyAddress.getId());
        assertNull(deletedAddress);

        Address alsoDeleted = addressDao.getByCompany(company);
        assertNull(alsoDeleted);

        Address alsoDeleted2 = addressDao.get(company);
        assertNull(alsoDeleted2);
    }
}