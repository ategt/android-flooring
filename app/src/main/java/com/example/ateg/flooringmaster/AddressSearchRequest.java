package com.example.ateg.flooringmaster;

import java.io.Serializable;

/**
 *
 * @author ATeg
 */
public class AddressSearchRequest implements Serializable{

    private AddressSearchByOptionEnum searchBy;
    private String searchText;

    public AddressSearchRequest() {
        this.searchText = "";
        this.searchBy = AddressSearchByOptionEnum.DEFAULT;
    }

    public AddressSearchRequest(String searchText, AddressSearchByOptionEnum searchBy) {
        this.searchText = searchText;
        this.searchBy = searchBy;
    }

    /**
     * @return the searchBy
     */
    public String getSearchBy() {
        if (searchBy == null){
            return AddressSearchByOptionEnum.DEFAULT.value(); 
        }
        return searchBy.value();
    }

    public AddressSearchByOptionEnum searchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = AddressSearchByOptionEnum.parse(searchBy);
    }

    public void setSearchByEnum(AddressSearchByOptionEnum searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
