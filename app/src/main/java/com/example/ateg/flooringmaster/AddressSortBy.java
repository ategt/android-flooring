package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 7/10/2017.
 */

public enum AddressSortBy {
    SORT_BY_COMPANY("company", AddressDao.SORT_BY_COMPANY),
    SORT_BY_ID("id", AddressDao.SORT_BY_ID),
    SORT_BY_FIRST_NAME("first_name", AddressDao.SORT_BY_FIRST_NAME),
    SORT_BY_LAST_NAME("last_name", AddressDao.SORT_BY_LAST_NAME);

    private String sortString;
    private Integer sortByInt;

    private AddressSortBy(String sortString, Integer sortByInt) {
        this.sortString = sortString;
        this.sortByInt = sortByInt;
    }

    public String value() {
        return sortString;
    }

    public int intValue() {
        return sortByInt;
    }

    public static AddressSortBy parse(Integer input) {
        for (AddressSortBy addressesSortBy : AddressSortBy.values()) {
            if (addressesSortBy.intValue() == input)
                return addressesSortBy;
        }
        return AddressSortBy.SORT_BY_ID;
    }

    public static AddressSortBy parse(String input) {
        for (AddressSortBy addressesSortBy : AddressSortBy.values()) {
            if (addressesSortBy.value() == input)
                return addressesSortBy;
        }

        for (AddressSortBy addressesSortBy : AddressSortBy.values()) {
            if (addressesSortBy.name() == input)
                return addressesSortBy;
        }

        for (AddressSortBy addressesSortBy : AddressSortBy.values()) {
            try {
                if (addressesSortBy.ordinal() == Integer.parseInt(input))
                    return addressesSortBy;
            } catch (NumberFormatException ex) {
            }
        }

        return AddressSortBy.SORT_BY_ID;
    }
}
