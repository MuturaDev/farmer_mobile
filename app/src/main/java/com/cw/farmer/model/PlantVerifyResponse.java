package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlantVerifyResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemsPlantVerify> pageItemsPlantVerify = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public PlantVerifyResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemsPlantVerify> getPageItemsPlantVerify() {
        return pageItemsPlantVerify;
    }

    public void setPageItemsPlantVerify(List<PageItemsPlantVerify> pageItemsPlantVerify) {
        this.pageItemsPlantVerify = pageItemsPlantVerify;
    }

    public PlantVerifyResponse withPageItemsPlantVerify(List<PageItemsPlantVerify> pageItemsPlantVerify) {
        this.pageItemsPlantVerify = pageItemsPlantVerify;
        return this;
    }

}