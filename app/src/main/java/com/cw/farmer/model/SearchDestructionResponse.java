package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDestructionResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemsDestruction> pageItemsDestruction = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public SearchDestructionResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemsDestruction> getPageItemsDestruction() {
        return pageItemsDestruction;
    }

    public void setPageItemsDestruction(List<PageItemsDestruction> pageItemsDestruction) {
        this.pageItemsDestruction = pageItemsDestruction;
    }

    public SearchDestructionResponse withPageItemsDestruction(List<PageItemsDestruction> pageItemsDestruction) {
        this.pageItemsDestruction = pageItemsDestruction;
        return this;
    }

}