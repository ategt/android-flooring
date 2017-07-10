package com.example.ateg.flooringmaster;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author ATeg
 */
public enum AddressSearchByOptionEnum {
    LAST_NAME("searchByLastName"),
    FIRST_NAME("searchByFirstName"),
    CITY("searchByCity"),
    STATE("searchByState"),
    ZIP("searchByZip"),
    COMPANY("searchByCompany"),
    STREET_NUMBER("searchByStreetNumber"),
    STREET_NAME("searchByStreetName"),
    STREET("searchByStreet"),
    NAME("searchByName"),
    NAME_OR_COMPANY("searchByNameOrCompany"),
    ALL("searchByAll"),
    DEFAULT("searchByAll");

    private String searchString;

    private AddressSearchByOptionEnum(String searchString) {
        this.searchString = searchString;
    }

    public String value() {
        return searchString;
    }

    public static AddressSearchByOptionEnum parse(String input) {

        for (AddressSearchByOptionEnum option : values()) {
            if (option.value().equalsIgnoreCase(input)) {
                return option;
            }
        }

        for (AddressSearchByOptionEnum option : values()) {
            if (option.name().equalsIgnoreCase(input)) {
                return option;
            }
        }

        for (AddressSearchByOptionEnum option : values()) {
            if (Integer.compare(option.ordinal(), Integer.parseInt(input)) == 0) {
                return option;
            }
        }

        return LAST_NAME;
    }
}
