package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 7/10/2017.
 */

public enum AddressSortByEnum {
    SORT_BY_COMPANY("company", AddressDao.SORT_BY_COMPANY),
    SORT_BY_ID("id", AddressDao.SORT_BY_ID),
    SORT_BY_FIRST_NAME("first_name", AddressDao.SORT_BY_FIRST_NAME),
    SORT_BY_LAST_NAME("last_name", AddressDao.SORT_BY_LAST_NAME);

    private String sortString;
    private Integer sortByInt;

    private AddressSortByEnum(String sortString, Integer sortByInt) {
        this.sortString = sortString;
        this.sortByInt = sortByInt;
    }

    public String value() {
        return sortString;
    }

    public int intValue() {
        return sortByInt;
    }

    public static AddressSortByEnum parse(Integer input) {
        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.intValue() == input)
                return addressesSortBy;
        }
        return AddressSortByEnum.SORT_BY_ID;
    }

    public static AddressSortByEnum parse(String input) {
        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.value() == input)
                return addressesSortBy;
        }

        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            if (addressesSortBy.name() == input)
                return addressesSortBy;
        }

        for (AddressSortByEnum addressesSortBy : AddressSortByEnum.values()) {
            try {
                if (addressesSortBy.ordinal() == Integer.parseInt(input))
                    return addressesSortBy;
            } catch (NumberFormatException ex) {
            }
        }

        return AddressSortByEnum.SORT_BY_ID;
    }
}
