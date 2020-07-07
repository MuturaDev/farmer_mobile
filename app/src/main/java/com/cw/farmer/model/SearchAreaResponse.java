package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAreaResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemSearchArea> pageItemSearchAreaList;


    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public List<PageItemSearchArea> getPageItemSearchAreaList() {
        return pageItemSearchAreaList;
    }
}
