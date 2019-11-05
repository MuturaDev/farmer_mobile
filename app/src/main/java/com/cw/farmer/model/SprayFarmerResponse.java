package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SprayFarmerResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemsSprayFarmer> pageItemsSprayFarmer = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public SprayFarmerResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemsSprayFarmer> getPageItemsSprayFarmer() {
        return pageItemsSprayFarmer;
    }

    public void setPageItemsSprayFarmer(List<PageItemsSprayFarmer> pageItemsSprayFarmer) {
        this.pageItemsSprayFarmer = pageItemsSprayFarmer;
    }

    public SprayFarmerResponse withPageItemsSprayFarmer(List<PageItemsSprayFarmer> pageItemsSprayFarmer) {
        this.pageItemsSprayFarmer = pageItemsSprayFarmer;
        return this;
    }

}