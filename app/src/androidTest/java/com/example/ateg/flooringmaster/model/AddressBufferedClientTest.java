package com.example.ateg.flooringmaster.model;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.ateg.flooringmaster.Address;
import com.example.ateg.flooringmaster.AddressBufferedClient;
import com.example.ateg.flooringmaster.AddressDao;
import com.example.ateg.flooringmaster.AddressDaoRemoteImpl;
import com.example.ateg.flooringmaster.AddressResultSegment;
import com.example.ateg.flooringmaster.AddressSortByEnum;
import com.example.ateg.flooringmaster.HttpUtilities;
import com.example.ateg.flooringmaster.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.example.ateg.flooringmaster.model.AddressTestUtilities.caseRandomizer;
import static com.example.ateg.flooringmaster.model.AddressTestUtilities.sortByLastNameComparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddressBufferedClientTest {

    AddressDao addressDao;
    Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        Uri testUri = Uri.parse(appContext.getString(R.string.starting_root_url));
        addressDao = new AddressDaoRemoteImpl(appContext, new HttpUtilities(appContext, testUri));

        addressDao = new AddressBufferedClient(addressDao, 20);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.ateg.flooringmaster", appContext.getPackageName());
    }


    @Test
    public void listTest() {
        List<Address> addresses = addressDao.list();

        Assert.assertTrue(addresses.size() > 10);
    }

    @Test
    public void getTest() {
        List<Address> addresses = addressDao.list();

        Random random = new Random();
        int randomAddressId = random.nextInt(addresses.size());

        Address randomAddress = addresses.get(randomAddressId);
        Assert.assertNotNull(randomAddress);

        Address specificAddress = addressDao.get(randomAddress.getId());

        Assert.assertNotNull(specificAddress);
        Assert.assertEquals(specificAddress, randomAddress);
    }

    @Test
    public void createTest() {

        Address address = AddressTest.addressGenerator();
        Assert.assertNotNull(address);
        Assert.assertNull(address.getId());

        Address addressReturned = addressDao.create(address);

        Assert.assertNotNull(addressReturned);
        Integer returnedAddressId = addressReturned.getId();

        Assert.assertTrue(returnedAddressId > 0);

        address.setId(returnedAddressId);

        Assert.assertEquals(addressReturned, address);

        int addressId = addressReturned.getId();
        Address storedAddress = addressDao.get(addressId);

        assertNotNull(storedAddress);
        Assert.assertEquals(storedAddress, addressReturned);
    }

    /**
     * Test of create method, of class AddressDaoPostgresImpl.
     */
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

        Address address = AddressTest.addressBuilder(city, company, firstName, lastName, state, streetName, streetNumber, zip);

        int beforeCreation = addressDao.size(true);

        Address result = AddressTest.addressCloner(addressDao.create(address));
        int afterCreation = addressDao.size(true);

        assertEquals(beforeCreation + 1, afterCreation);

        assertNotNull(result);
        assertNotNull(result.getId());

        assertTrue(result.getId() > 0);

        Address retrivedAddress = addressDao.get(result.getId());
        assertEquals(retrivedAddress, result);

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

    @Test
    public void sizesAllReturnSameValue(){
        int preBuffer = addressDao.size();

        int blocked = addressDao.size(true);
        int notBlocked = addressDao.size(false);
        int postBlocked = addressDao.size();

        assertEquals(blocked, notBlocked);
        assertEquals(blocked, postBlocked);
    }

    /**
     * Test of list method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testList() {
        System.out.println("list");

        Address address = AddressTest.addressGenerator();
        address = addressDao.create(address);

        List<Address> list = addressDao.list();

        assertEquals(list.size(), addressDao.size(true));

        assertTrue(list.contains(address));
    }

    /**
     * Test of searchByLastName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByLastName() {
        System.out.println("searchByLastName");
        String lastName = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setLastName(lastName);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByLastName(lastName);

        assertNotNull(result);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByLastName(lastName.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByLastName(lastName.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByFirstName() {
        System.out.println("searchByFirstName");

        String firstName = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setFirstName(firstName);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByFirstName(firstName);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByFirstName(firstName.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByFirstName(firstName.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByFirstName method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByCompany() {
        System.out.println("searchByCompany");

        String company = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setCompany(company);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByCompany(company);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByCompany(company.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCompany(company.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByCity method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByCity() {
        System.out.println("searchByCity");
        String city = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setCity(city);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByCity(city);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByCity(city.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByCity(city.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByState method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByState() {
        System.out.println("searchByState");

        String state = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setState(state);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByState(state);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByState(state.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByState(state.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByZip() {
        System.out.println("searchByZip");

        String zip = UUID.randomUUID().toString();

        Address address = AddressTest.addressGenerator();
        address.setZip(zip);
        address = addressDao.create(address);

        List<Address> result = addressDao.searchByZip(zip);
        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        result = addressDao.searchByZip(zip.toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.toUpperCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5));
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20));
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20).toLowerCase());
        assertTrue(result.contains(address));

        result = addressDao.searchByZip(zip.substring(5, 20).toUpperCase());
        assertTrue(result.contains(address));

    }

    @Test
    public void testGetWithString() {
        System.out.println("searchWithGet");
        final Random random = new Random();

        for (int pass = 0; pass < 25; pass++) {

            String[] randomStrings = new String[8];

            for (int i = 0; i < randomStrings.length; i++) {
                randomStrings[i] = UUID.randomUUID().toString();
            }

            Address address = AddressTest.addressBuilder(randomStrings[0],
                    randomStrings[1],
                    randomStrings[2],
                    randomStrings[3],
                    randomStrings[4],
                    randomStrings[5],
                    randomStrings[6],
                    randomStrings[7]);

            address = addressDao.create(address);
            int resultId = address.getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            Address result = addressDao.get(searchString);

            assertEquals(result, address);
            addressDao.delete(resultId);
        }

        for (int pass = 0; pass < 150; pass++) {

            String[] randomStrings = new String[8];

            for (int i = 0; i < randomStrings.length; i++) {
                randomStrings[i] = UUID.randomUUID().toString();
                randomStrings[i] = caseRandomizer(random, randomStrings[i]);
            }

            Address address = AddressTest.addressBuilder(randomStrings[0],
                    randomStrings[1],
                    randomStrings[2],
                    randomStrings[3],
                    randomStrings[4],
                    randomStrings[5],
                    randomStrings[6],
                    randomStrings[7]);

            address = addressDao.create(address);
            int resultId = address.getId();

            int position = new Random().nextInt(randomStrings.length);
            String searchString = randomStrings[position];

            int minimumStringLength = 10;
            int processLength = searchString.length() - minimumStringLength;
            int startingPostition = random.nextInt(processLength - minimumStringLength);
            int endingPostition = random.nextInt(processLength - startingPostition) + startingPostition + minimumStringLength;

            searchString = searchString.substring(startingPostition, endingPostition);
            searchString = caseRandomizer(random, searchString);

            Address result = addressDao.get(searchString);

            assertEquals(result, address);
            addressDao.delete(resultId);
        }
    }

    @Test
    public void getSortedByName() {
        List<Address> addresse1s = addressDao.list();
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.SORT_BY_LAST_NAME, 0, Integer.MAX_VALUE));

        Address[] addressArray = addresse1s.toArray(new Address[addresse1s.size()]);
        Arrays.sort(addressArray, sortByLastNameComparator());

        for (int i = 0; i < addressArray.length; i++) {
            assertEquals(addressArray[i], addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByNameUsingSortByParam() {
        List<Address> addresses = addressDao.list();
        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("last_name");

        Address[] addressArray = addresses.toArray(new Address[addresses.size()]);
        Arrays.sort(addressArray, sortByLastNameComparator());

        for (int i = 0; i < addressArray.length; i++) {

            assertEquals(" First Incongruency at : " + i + " compared: " + addressArray[i].getId() + " \t Db: " + addressesFromDb.get(i).getId(), addressArray[i], addressesFromDb.get(i));

        }
    }

    @Test
    public void getSortedByIdUsingSortByParam() {
        List<Address> addresses = addressDao.list(new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, 0, Integer.MAX_VALUE));
        List<Address> addressesFromDb = addressDao.getAddressesSortedByParameter("id");

        for (int i = 0; i < addresses.size(); i++) {

            assertEquals(addresses.get(i), addressesFromDb.get(i));

        }
    }
}