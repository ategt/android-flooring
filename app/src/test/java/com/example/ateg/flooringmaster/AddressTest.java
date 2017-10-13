/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ateg.flooringmaster;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author ATeg
 */
public class AddressTest {
    
    public AddressTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Address.
     */
    @Test
    public void testEquals() {
        System.out.println("Equals");
        
        Random random = new Random();
        
        Address instanceA = new Address();
        Address instanceB = new Address();
        Address instanceC = new Address();
        
        assertEquals(instanceA, instanceA);
        assertEquals(instanceA, instanceB);
        assertEquals(instanceA, instanceC);
        assertEquals(instanceB, instanceA);
        
        assertTrue(instanceA.equals(instanceB));
        assertTrue(instanceA.equals(instanceC));
        assertTrue(instanceC.equals(instanceA));
        
        int id = random.nextInt();
        String city = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String zip = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String streetNumber = UUID.randomUUID().toString();
        String streetName = UUID.randomUUID().toString();
        
        instanceA.setId(id);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setId(id);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setCity(city);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setCity(city);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setCompany(company);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setCompany(company);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setFirstName(firstName);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setFirstName(firstName);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setLastName(lastName);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setLastName(lastName);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setState(state);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setState(state);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setStreetName(streetName);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setStreetName(streetName);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setStreetNumber(streetNumber);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setStreetNumber(streetNumber);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
        
        instanceA.setZip(zip);
        
        assertNotEquals(instanceA, instanceB);
        assertFalse(instanceB.equals(instanceA));
        
        instanceB.setZip(zip);
        
        assertEquals(instanceA, instanceB);
        assertTrue(instanceB.equals(instanceA));
    }

    /**
     * Test of setId method, of class Address.
     */
    @Test
    public void testHash() {
        System.out.println("HashCode");
                
        Address instance = new Address();
        assertNotNull(instance.hashCode());
        
        assertTrue(instance.hashCode() > -1);
    }

    public static Address addressGenerator() {
        String city = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String zip = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String streetNumber = UUID.randomUUID().toString();
        String streetName = UUID.randomUUID().toString();

        Address address = addressBuilder(city, company, firstName, lastName, state, streetName, streetNumber, zip);
        return address;
    }


    public static Address addressCloner(Address address) {
        Address address1 = addressBuilder(
                address.getCity(),
                address.getCompany(),
                address.getFirstName(),
                address.getLastName(),
                address.getState(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getZip()
        );

        if (address.getId() != null)
            address1.setId(address.getId());

        assertEquals(address, address1);
        return address1;
    }

    public static Address addressBuilder(String city, String company, String firstName, String lastName, String state, String streetName, String streetNumber, String zip) {
        Address address = new Address();
        address.setCity(city);
        address.setCompany(company);
        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setState(state);
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);
        address.setZip(zip);
        return address;
    }
}
