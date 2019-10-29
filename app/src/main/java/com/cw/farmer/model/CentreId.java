package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CentreId {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("centerId")
    @Expose
    private int centerId;
    @SerializedName("centreName")
    @Expose
    private String centreName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CentreId withId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CentreId withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public CentreId withCenterId(int centerId) {
        this.centerId = centerId;
        return this;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public CentreId withCentreName(String centreName) {
        this.centreName = centreName;
        return this;
    }

}