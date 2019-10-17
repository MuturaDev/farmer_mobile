package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TasksResponse {

    @SerializedName("totalFilteredRecords")
    @Expose
    private int totalFilteredRecords;
    @SerializedName("pageItems")
    @Expose
    private List<PageItemstask> pageItemstasks = null;

    public int getTotalFilteredRecords() {
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public TasksResponse withTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
        return this;
    }

    public List<PageItemstask> getPageItemstasks() {
        return pageItemstasks;
    }

    public void setPageItemstasks(List<PageItemstask> pageItemstasks) {
        this.pageItemstasks = pageItemstasks;
    }

    public TasksResponse withPageItemstasks(List<PageItemstask> pageItemstasks) {
        this.pageItemstasks = pageItemstasks;
        return this;
    }

}