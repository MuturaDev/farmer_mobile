package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantBlockResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemPlantBlock> pageItemPlantBlocksList;


    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public List<PageItemPlantBlock> getPageItemPlantBlocksList() {
        return pageItemPlantBlocksList;
    }
}
