package com.example.ateg.flooringmaster;

/**
 *
 * @author ATeg
 */
public class ResultProperties {

    private final Integer pageNumber;
    private final Integer resultsPerPage;
    private final AddressSortByEnum sortByEnum;

    public ResultProperties(AddressSortByEnum sortByEnum, Integer pageNumber, Integer resultsPerPage) {
        this(pageNumber, resultsPerPage, sortByEnum);
    }

    public ResultProperties(Integer pageNumber, Integer resultsPerPage, AddressSortByEnum sortByEnum) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.sortByEnum = sortByEnum;
    }

    /**
     * @return the pageNumber
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @return the resultsPerPage
     */
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * @return the sortByEnum
     */
    public AddressSortByEnum getSortByEnum() {
        return sortByEnum;
    }
}