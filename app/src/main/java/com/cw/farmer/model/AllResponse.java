package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllResponse {

    @SerializedName("resourceId")
    @Expose
    private int resourceId;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public AllResponse withResourceId(int resourceId) {
        this.resourceId = resourceId;
        return this;
    }

}