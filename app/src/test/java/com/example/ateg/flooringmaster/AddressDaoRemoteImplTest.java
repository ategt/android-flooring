package com.example.ateg.flooringmaster;

import android.net.Uri;
import android.util.Log;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 * Created by ATeg on 6/29/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Uri.class})
public class AddressDaoRemoteImplTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void get() throws Exception {

        AddressDao addressDao = new AddressDaoRemoteImpl(null, new HttpUtilities(null, Uri.EMPTY));

        Address address = addressDao.get(1);

        Assert.assertEquals((long)address.getId(), 3L);
        Assert.assertEquals(address.getCity(), 3L);
        Assert.assertEquals(address.getCompany(), 3L);
        Assert.assertEquals(address.getFirstName(), 3L);
        Assert.assertEquals(address.getLastName(), 3L);
        Assert.assertEquals(address.getState(), 3L);
        Assert.assertEquals(address.getStreetName(), 3L);
        Assert.assertEquals(address.getStreetNumber(), 3L);
        Assert.assertEquals(address.getZip(), 3L);

    }

    @Test
    public void get1() throws Exception {

    }

    @Test
    public void getByCompany() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void size() throws Exception {

    }

    @Test
    public void getAddressesSortedByParameter() throws Exception {

    }

    @Test
    public void getCompletionGuesses() throws Exception {

    }

    @Test
    public void list() throws Exception {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Uri.class);

        Uri uri = mock(Uri.class);

        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
        PowerMockito.when(uri, "toString").thenReturn("http://127.0.0.1:8080");

        //uri.get
        //Uri.Builder();

        //new android.net.Uri.StringUri();

        Uri testUri = Uri.parse("http://127.0.0.1:8080");

        String host = testUri.getHost();
        int port = testUri.getPort();

        AddressDao addressDao = new AddressDaoRemoteImpl(null,
                new HttpUtilities(null, testUri));

        List<Address> addresses = addressDao.list();

        Assert.assertTrue(addresses.size() > 10);

    }

    @Test
    public void list1() throws Exception {

    }

    @Test
    public void searchByFirstName() throws Exception {

    }

    @Test
    public void searchByLastName() throws Exception {

    }

    @Test
    public void searchByCity() throws Exception {

    }

    @Test
    public void searchByCompany() throws Exception {

    }

    @Test
    public void searchByState() throws Exception {

    }

    @Test
    public void searchByZip() throws Exception {

    }

}