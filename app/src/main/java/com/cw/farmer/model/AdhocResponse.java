package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdhocResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemsAdhoc> pageItemsAdhoc = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public AdhocResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemsAdhoc> getPageItemsAdhoc() {
        return pageItemsAdhoc;
    }

    public void setPageItemsAdhoc(List<PageItemsAdhoc> pageItemsAdhoc) {
        this.pageItemsAdhoc = pageItemsAdhoc;
    }

    public AdhocResponse withPageItemsAdhoc(List<PageItemsAdhoc> pageItemsAdhoc) {
        this.pageItemsAdhoc = pageItemsAdhoc;
        return this;
    }

}