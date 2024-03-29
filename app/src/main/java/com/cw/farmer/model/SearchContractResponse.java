package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchContractResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<SearchContractPageItem> pageItems = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public SearchContractResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<SearchContractPageItem> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<SearchContractPageItem> pageItems) {
        this.pageItems = pageItems;
    }

    public SearchContractResponse withPageItems(List<SearchContractPageItem> pageItems) {
        this.pageItems = pageItems;
        return this;
    }

}