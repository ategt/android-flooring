package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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
@Config(constants = BuildConfig.class, sdk = 21)
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

    /**
     * Test of create method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testCRUDold() {
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

        int beforeCreation = addressDao.size();
        Address result = addressDao.create(address);
        int afterCreation = addressDao.size();

        assertEquals(beforeCreation + 1, afterCreation);

        assertNotNull(result);
        assertNotNull(result.getId());

        assertTrue(result.getId() > 0);

        Address retrivedAddress = addressDao.get(result.getId());
        assertEquals(retrivedAddress, result);

        retrivedAddress.setCity(UUID.randomUUID().toString());

        addressDao.update(retrivedAddress);

        Address companyAddress = addressDao.get(retrivedAddress.getCompany());

        assertEquals(companyAddress, retrivedAddress);
        assertNotEquals(result, companyAddress);

        assertEquals(afterCreation, addressDao.size());

        addressDao.delete(companyAddress.getId());

        assertEquals(afterCreation - 1, addressDao.size());

        Address deletedAddress = addressDao.get(companyAddress.getId());
        assertNull(deletedAddress);

        Address alsoDeleted = addressDao.get(company);
        assertNull(alsoDeleted);

        Address alsoDeleted2 = addressDao.get(company);
        assertNull(alsoDeleted2);
    }

    /**
     * Test of list method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testList() {
        System.out.println("list");

        Address address = AddressTest.addressGenerator();
        addressDao.create(address);

        List<Address> list = addressDao.list(null);

        assertEquals(list.size(), addressDao.size());

        assertTrue(list.contains(address));
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByParam() {
        System.out.println("searchByStreetName By Param");

        Address address = AddressTest.addressGenerator();

        String streetName = address.getStreetName();
        address = addressDao.create(address);

        String queryString = streetName;
        AddressSearchByOptionEnum searchOption = AddressSearchByOptionEnum.STREET_NAME;
        AddressSearchByOptionEnum wrongSearchOption = AddressSearchByOptionEnum.NAME_OR_COMPANY;
        Integer page = 0;
        Integer resultsPerPage = 20;

        List<Address> result = addressDao.search(new AddressSearchRequest(queryString, searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));

        assertTrue(result.contains(address));
        assertEquals(result.size(), 1);

        List<Address> resultEmpty = addressDao.search(new AddressSearchRequest(queryString, wrongSearchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(resultEmpty.isEmpty());
        assertEquals(resultEmpty.size(), 0);

        result = addressDao.search(new AddressSearchRequest(queryString.toLowerCase(), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(queryString.toUpperCase(), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(queryString.substring(5), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

        result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption),
                new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
        assertTrue(result.contains(address));

    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByEverythingParam() {
        System.out.println("searchByEverything By Param");

        AddressSearchByOptionEnum[] searchOptionEnum = AddressSearchByOptionEnum.values();
        for (AddressSearchByOptionEnum searchOption : searchOptionEnum) {

            Address address = AddressTest.addressGenerator();

            String queryString = null;
            AddressSearchRequest addressSearchRequest;
            int size;

            switch (searchOption) {
                case ALL:
                    queryString = address.getFirstName();
                    break;
                case CITY:
                    queryString = address.getCity();
                    break;
                case COMPANY:
                    queryString = address.getCompany();
                    break;
                case DEFAULT:
                    queryString = address.getFirstName();
                    break;
                case FIRST_NAME:
                    queryString = address.getFirstName();
                    break;
                case LAST_NAME:
                    queryString = address.getLastName();
                    break;
                case NAME:
                    queryString = address.getFirstName() + " " + address.getLastName();
                    break;
                case NAME_OR_COMPANY:
                    queryString = address.getCompany();
                    break;
                case STATE:
                    queryString = address.getState();
                    break;
                case STREET:
                    queryString = address.getStreetNumber() + " " + address.getStreetName();
                    break;
                case STREET_NAME:
                    queryString = address.getStreetName();
                    break;
                case STREET_NUMBER:
                    queryString = address.getStreetNumber();
                    break;
                case ZIP:
                    queryString = address.getZip();
                    break;
                default:
                    throw new IndexOutOfBoundsException("Test Execution should not get here.");
            }

            address = addressDao.create(address);

            Integer page = 0;
            Integer resultsPerPage = 20;

            List<Address> result = addressDao.search(new AddressSearchRequest(queryString, searchOption),
                    new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));

            StringBuffer stringBuffer = new StringBuffer();
            for (Address address1 : result) {
                stringBuffer.append(address1.getId() + ", ");
            }

            assertEquals(searchOption.name() + ": " + queryString + "\t "
                            + stringBuffer.toString(),
                    result.size(),
                    1);

            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString, searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.toLowerCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.toUpperCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());
        }
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByEverythingParamWithRandomNumbers() {
        System.out.println("searchByEverything By Param");

        Random random = new Random();

        AddressSearchByOptionEnum[] searchOptionEnum = AddressSearchByOptionEnum.values();
        for (AddressSearchByOptionEnum searchOption : searchOptionEnum) {

            Address address = AddressTest.addressGenerator();

            String queryString = null;
            AddressSearchRequest addressSearchRequest;

            switch (searchOption) {
                case ALL:
                    queryString = address.getFirstName();
                    break;
                case CITY:
                    queryString = address.getCity();
                    break;
                case COMPANY:
                    queryString = address.getCompany();
                    break;
                case DEFAULT:
                    queryString = address.getFirstName();
                    break;
                case FIRST_NAME:
                    queryString = address.getFirstName();
                    break;
                case LAST_NAME:
                    queryString = address.getLastName();
                    break;
                case NAME:
                    queryString = address.getFirstName() + " " + address.getLastName();
                    break;
                case NAME_OR_COMPANY:
                    queryString = address.getCompany();
                    break;
                case STATE:
                    queryString = address.getState();
                    break;
                case STREET:
                    queryString = address.getStreetNumber() + " " + address.getStreetName();
                    break;
                case STREET_NAME:
                    queryString = address.getStreetName();
                    break;
                case STREET_NUMBER:
                    queryString = address.getStreetNumber();
                    break;
                case ZIP:
                    queryString = address.getZip();
                    break;
                default:
                    throw new IndexOutOfBoundsException("Test Execution should not get here.");
            }

            address = addressDao.create(address);

            Integer resultsPerPage = Math.abs(random.nextInt());

            List<Address> result = addressDao.search(new AddressSearchRequest(queryString, searchOption), new AddressResultSegment(null, 0, resultsPerPage));

            assertEquals(searchOption.name() + ": " + queryString + "\t "
                            + getResultsFromString(result),
                    result.size(),
                    1);

            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString, searchOption);
            int size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            Integer page = 0;

            result = addressDao.search(new AddressSearchRequest(queryString.toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));

            List<Address> additionalPages = new ArrayList();
            while (!additionalPages.isEmpty()) {
                page++;
                additionalPages = addressDao.search(new AddressSearchRequest(queryString.toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
                result.addAll(additionalPages);
                assertTrue(additionalPages.size() <= resultsPerPage);
            }

            page = 0;

            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.toLowerCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));

            additionalPages.clear();
            while (!additionalPages.isEmpty()) {
                page++;
                additionalPages = addressDao.search(new AddressSearchRequest(queryString.toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
                result.addAll(additionalPages);
                assertTrue(additionalPages.size() <= resultsPerPage);
            }

            page = 0;

            assertTrue(result.contains(address));

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));
            assertTrue(result.size() <= resultsPerPage);

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));
            assertTrue(result.size() <= resultsPerPage);

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));
            assertTrue(result.size() <= resultsPerPage);

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));
            assertTrue(result.size() <= resultsPerPage);
        }
    }

    private Object getResultsFromString(List<Address> result) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Address address1 : result) {
            stringBuffer.append(address1.getId() + ", ");
        }
        return stringBuffer.toString();
    }

    /**
     * Test of searchByZip method, of class AddressDaoPostgresImpl.
     */
    @Test
    public void testSearchByEverythingParamWithNullNumbers() {
        System.out.println("searchByEverything By Param");

        Random random = new Random();

        AddressSearchByOptionEnum[] searchOptionEnum = AddressSearchByOptionEnum.values();
        for (AddressSearchByOptionEnum searchOption : searchOptionEnum) {

            Address address = AddressTest.addressGenerator();

            String queryString = null;
            AddressSearchRequest addressSearchRequest;

            switch (searchOption) {
                case ALL:
                    queryString = address.getFirstName();
                    break;
                case CITY:
                    queryString = address.getCity();
                    break;
                case COMPANY:
                    queryString = address.getCompany();
                    break;
                case DEFAULT:
                    queryString = address.getFirstName();
                    break;
                case FIRST_NAME:
                    queryString = address.getFirstName();
                    break;
                case LAST_NAME:
                    queryString = address.getLastName();
                    break;
                case NAME:
                    queryString = address.getFirstName() + " " + address.getLastName();
                    break;
                case NAME_OR_COMPANY:
                    queryString = address.getCompany();
                    break;
                case STATE:
                    queryString = address.getState();
                    break;
                case STREET:
                    queryString = address.getStreetNumber() + " " + address.getStreetName();
                    break;
                case STREET_NAME:
                    queryString = address.getStreetName();
                    break;
                case STREET_NUMBER:
                    queryString = address.getStreetNumber();
                    break;
                case ZIP:
                    queryString = address.getZip();
                    break;
                default:
                    throw new IndexOutOfBoundsException("Test Execution should not get here.");
            }

            address = addressDao.create(address);

            Integer resultsPerPage = null;
            Integer page = null;

            List<Address> result = addressDao.search(new AddressSearchRequest(queryString, searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));

            assertEquals(searchOption.name() + ": " + queryString + "\t "
                            + getResultsFromString(result),
                    result.size(),
                    1);

            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString, searchOption);
            int size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.toLowerCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.toUpperCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20).toLowerCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());

            result = addressDao.search(new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption), new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, page, resultsPerPage));
            assertTrue(result.contains(address));

            addressSearchRequest = new AddressSearchRequest(queryString.substring(5, 20).toUpperCase(), searchOption);
            size = addressDao.size(addressSearchRequest);
            assertEquals(size, result.size());
        }
    }

    @Test
    public void testSizeWithNull() {
        Integer size = addressDao.size(null);
        assertTrue("It just had to get to here.", true);
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

            int resultId = addressDao.create(address).getId();

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

            int resultId = addressDao.create(address).getId();

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
    public void getSortedByNameUsingSortByParam() {
        List<Address> addresses = addressDao.list(null);
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), null, null));

        removeAddressesWithNullOrEmptyFields(addresses, addresses, addressesFromDb);

        Collections.sort(addresses, sortByLastName());

        for (int i = 0; i < addresses.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPagination() {
        List<Address> addresses = addressDao.list(null);
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 0, 20));

        assertEquals(addressesFromDb.size(), 20);

        Collections.sort(addresses, sortByLastName());

        for (int i = 0; i < addressesFromDb.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPagination1() {
        List<Address> addresses = addressDao.list(null);
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 0, 20));
        List<Address> addressesFromDb2 = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 1, 20));

        addressesFromDb.addAll(addressesFromDb2);

        assertEquals(addressesFromDb.size(), 40);

        Collections.sort(addresses, sortByLastName());

        for (int i = 0; i < addressesFromDb.size(); i++) {
            assertEquals(addresses.get(i), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPaginationUsingOffset() {
        List<Address> addresses = addressDao.list(null);
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 1, 30));

        assertEquals(addressesFromDb.size(), 30);

        Collections.sort(addresses, sortByLastName());

        for (int i = 0, r = 30; i < addressesFromDb.size(); i++, r++) {
            assertEquals(addresses.get(r), addressesFromDb.get(i));
        }
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPaginationWithOverflow() {
        List<Address> addressesSortedWithComparator = addressDao.list(null);

        int size = addressesSortedWithComparator.size();

        List<Address> addressesSortedWithDatabase = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 0, size + 10));

        assertEquals(addressesSortedWithDatabase.size(), size);

        removeAddressesWithNullOrEmptyFields(addressesSortedWithComparator, addressesSortedWithComparator, addressesSortedWithDatabase);

        assertTrue(addressesSortedWithDatabase.size() > 50);
        assertTrue(addressesSortedWithComparator.size() > 50);

        Collections.sort(addressesSortedWithComparator, sortByLastName());

        for (int i = 0; i < addressesSortedWithDatabase.size(); i++) {

            assertEquals("One Page, Oversized Result Param - ComparedId: " + addressesSortedWithComparator.get(i).getId() + ", DatabaseId: " + addressesSortedWithDatabase.get(i).getId(), addressesSortedWithComparator.get(i), addressesSortedWithDatabase.get(i));

        }
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPaginationWithOverflow2() {
        List<Address> addresses = addressDao.list(null);

        int size = addresses.size();
        int pages = size / 50;

        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), pages + 1, 50));

        assertEquals(addressesFromDb.size(), 0);
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPaginationWithUnderflow() {
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), 1, 0));

        assertEquals(addressesFromDb.size(), 0);
    }

    @Test
    public void getSortedByNameUsingSortByParamAndPaginationWithRandomNumbers() {
        Random random = new Random();

        for (int testPass = 0; testPass < 150; testPass++) {
            int pageNumber = random.nextInt();
            int resultsPerPage = random.nextInt();

            List<Address> addressesSortedWithComparator = addressDao.list(null);
            List<Address> addressesSortedWithDatabase = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("last_name"), pageNumber, resultsPerPage));

            assertTrue(addressesSortedWithDatabase.size() <= addressesSortedWithComparator.size());

            if ((long) pageNumber * (long) resultsPerPage > addressesSortedWithComparator.size()) {
                if (pageNumber >= 0 && resultsPerPage >= 0) {
                    assertEquals("Page Number Is Beyond Result Range.\n PageNum: " + pageNumber + " ResultsPerPage: " + resultsPerPage, addressesSortedWithDatabase.size(), 0);
                } else {
                    assertEquals("One Page Containing All The Results Is Expected.\n PageNum: " + pageNumber + " ResultsPerPage: " + resultsPerPage, addressesSortedWithDatabase.size(), addressesSortedWithComparator.size());
                }
            } else if (pageNumber >= 0 && resultsPerPage >= 0) {
                assertEquals("A Typical Page Within The Expected Range.\n PageNum: " + pageNumber + " ResultsPerPage: " + resultsPerPage, addressesSortedWithDatabase.size(), resultsPerPage);
            }

            if (!(pageNumber >= 0 && resultsPerPage >= 0)) {
                pageNumber = 0;
                resultsPerPage = 0;
            }

            removeAddressesWithNullOrEmptyFields(addressesSortedWithComparator, addressesSortedWithComparator, addressesSortedWithDatabase);

            if ((long) pageNumber * (long) resultsPerPage > addressesSortedWithComparator.size() && (pageNumber >= 0 && resultsPerPage >= 0)) {
                assertTrue(addressesSortedWithDatabase.isEmpty());
            } else {
                assertTrue(addressesSortedWithDatabase.size() > 50);
            }

            assertTrue(addressesSortedWithComparator.size() > 50);

            Collections.sort(addressesSortedWithComparator, sortByLastName());

            for (int resultIdFromComparator = pageNumber * resultsPerPage, resultIdFromDatabase = 0; resultIdFromDatabase < addressesSortedWithDatabase.size(); resultIdFromComparator++, resultIdFromDatabase++) {

                assertEquals("TestPass:" + testPass + ", ComparatorPos:" + resultIdFromComparator + ", DbId:" + resultIdFromDatabase + " - PageNum: " + pageNumber
                                + " ResultsPerPage: " + resultsPerPage
                                + " - ComparedId: " + addressesSortedWithComparator.get(resultIdFromComparator).getId() + ", DatabaseId: " + addressesSortedWithDatabase.get(resultIdFromDatabase).getId(),
                        addressesSortedWithComparator.get(resultIdFromComparator),
                        addressesSortedWithDatabase.get(resultIdFromDatabase));

            }
        }
    }

    private void removeAddressesWithNullOrEmptyFields(List<Address> processAddressList, List<Address>... clearableAddressList) {

        List<Address> removableObjects = new ArrayList<>();
        removableObjects.addAll(processAddressList);

        for (Address address : processAddressList) {

            if (
                    Strings.nullToEmpty(address.getFirstName()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getLastName()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getCompany()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getState()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getCity()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getStreetName()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getStreetNumber()).trim().isEmpty() &&
                            Strings.nullToEmpty(address.getZip()).trim().isEmpty()) {
                removableObjects.add(address);
            }
        }

        for (List<Address> clearingList : clearableAddressList) {
            clearingList.removeAll(removableObjects);
        }
    }

    @NonNull
    private static Comparator<Address> sortByLastName() {
        return new Comparator<Address>() {
            @Override
            public int compare(Address address1, Address address2) {
                int result = 0;

                if (address1.getLastName() == null && address2.getLastName() == null) {
                    result = 0;
                } else if (address1.getLastName() == null || address2.getLastName() == null) {
                    if (address1.getLastName() == null) {
                        return 1;
                    } else if (address2.getLastName() == null) {
                        return -1;
                    } else {
                        throw new IllegalStateException("This should not be possible.");
                    }
                }

                if (result == 0) {
                    result = Strings.nullToEmpty(address1.getLastName()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getLastName()).toLowerCase());
                }

                if (result == 0) {
                    if (address1.getFirstName() == null && address2.getFirstName() == null) {
                        result = 0;
                    } else if (address1.getFirstName() == null || address2.getFirstName() == null) {
                        if (address1.getFirstName() == null) {
                            result = 1;
                        } else if (address2.getFirstName() == null) {
                            result = -1;
                        } else {
                            throw new IllegalStateException("This should not be possible.");
                        }
                    } else {
                        result = Strings.nullToEmpty(address1.getFirstName()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getFirstName()).toLowerCase());
                    }
                }

                if (result == 0) {
                    if (Strings.isNullOrEmpty(address1.getCompany())
                            && !Strings.isNullOrEmpty(address2.getCompany())) {
                        return 1;
                    } else if (!Strings.isNullOrEmpty(address1.getCompany())
                            && Strings.isNullOrEmpty(address2.getCompany())) {
                        return -1;
                    }
                }

                if (result == 0) {
                    result = Strings.nullToEmpty(address1.getCompany()).toLowerCase().compareTo(Strings.nullToEmpty(address2.getCompany()).toLowerCase());
                }

                if (result == 0) {
                    result = Integer.compare(address1.getId(), address2.getId());
                }

                return result;
            }
        };
    }

    @Test
    public void getSortedByIdUsingSortByParam() {
        List<Address> addresses = addressDao.list(new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, null, null));
        List<Address> addressesFromDb = addressDao.list(new AddressResultSegment(AddressSortByEnum.parse("id"), null, null));

        for (int i = 0; i < addresses.size(); i++) {

            assertEquals(addresses.get(i), addressesFromDb.get(i));

        }
    }

    private String caseRandomizer(final Random random, String input) {
        switch (random.nextInt(6)) {

            case 0:
                input = input;
                break;
            case 1:
                input = input.toLowerCase();
                break;
            case 2:
                input = input.toUpperCase();
                break;
            default:
                char[] charArray = input.toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    switch (random.nextInt(4)) {
                        case 1:
                            charArray[j] = Character.toLowerCase(charArray[j]);
                            break;
                        case 2:
                            charArray[j] = Character.toUpperCase(charArray[j]);
                            break;
                        case 3:
                            charArray[j] = Character.toTitleCase(charArray[j]);
                            break;
                        default:
                            charArray[j] = charArray[j];
                            break;
                    }

                    input = new String(charArray);
                }
        }

        return input;
    }

}