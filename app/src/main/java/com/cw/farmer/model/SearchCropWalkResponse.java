package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchCropWalkResponse {
    @SerializedName("totalFilteredRecords")
    @Expose
    private Integer totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<CropWalkPageItemResponse> pageItems = null;


    public Integer getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public List<CropWalkPageItemResponse> getPageItems() {
        return pageItems;
    }
}
