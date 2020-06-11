package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HarvestBlockResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemHarvestBlocks> pageItemHarvestBlocksList;


    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public List<PageItemHarvestBlocks> getPageItemHarvestBlocksList() {
        return pageItemHarvestBlocksList;
    }
}
