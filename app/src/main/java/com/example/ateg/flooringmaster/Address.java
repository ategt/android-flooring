package com.example.ateg.flooringmaster;

import java.util.Objects;

/**
 * Created by ATeg on 6/1/2017.
 */

public class Address {

    private Integer id;
    private String firstName;
    private String lastName;
    private String company;
    private String streetName;
    private String streetNumber;
    private String city;
    private String state;
    private String zip;

    @Override
    public boolean equals(Object object){
        if (object == null){
            return false;
        }

        if (object instanceof Address){
            Address otherAddress = (Address)object;

            return Objects.equals(getCity(), otherAddress.getCity()) &&
                    Objects.equals(getCompany(), otherAddress.getCompany()) &&
                    Objects.equals(getFirstName(), otherAddress.getFirstName()) &&
                    Objects.equals(getId(), otherAddress.getId()) &&
                    Objects.equals(getLastName(), otherAddress.getLastName()) &&
                    Objects.equals(getState(), otherAddress.getState()) &&
                    Objects.equals(getStreetName(), otherAddress.getStreetName()) &&
                    Objects.equals(getStreetNumber(), otherAddress.getStreetNumber()) &&
                    Objects.equals(getZip(), otherAddress.getZip());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        if (id == null) return 0;
        return id;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the streetName
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * @param streetName the streetName to set
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * @return the streetNumber
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }
}
