package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerHarvestResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemHarvest> pageItemHarvest = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public FarmerHarvestResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemHarvest> getPageItemHarvest() {
        return pageItemHarvest;
    }

    public void setPageItemHarvest(List<PageItemHarvest> pageItemHarvest) {
        this.pageItemHarvest = pageItemHarvest;
    }

    public FarmerHarvestResponse withPageItemHarvest(List<PageItemHarvest> pageItemHarvest) {
        this.pageItemHarvest = pageItemHarvest;
        return this;
    }

}