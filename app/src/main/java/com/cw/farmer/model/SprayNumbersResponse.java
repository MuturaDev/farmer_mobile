package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SprayNumbersResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemsSprayNumber> pageItemsSprayNumbers = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public SprayNumbersResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemsSprayNumber> getPageItemsSprayNumbers() {
        return pageItemsSprayNumbers;
    }

    public void setPageItemsSprayNumbers(List<PageItemsSprayNumber> pageItemsSprayNumbers) {
        this.pageItemsSprayNumbers = pageItemsSprayNumbers;
    }

    public SprayNumbersResponse withPageItemsSprayNumbers(List<PageItemsSprayNumber> pageItemsSprayNumbers) {
        this.pageItemsSprayNumbers = pageItemsSprayNumbers;
        return this;
    }

}