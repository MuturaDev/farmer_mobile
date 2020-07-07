package com.cw.farmer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PageItemSearchArea implements Serializable {


    @SerializedName("id")
    private int id;
    @SerializedName("farmName")
    private String farmName;
    @SerializedName("dateCreated")
    private List<Integer> dateCreated;
    @SerializedName("username")
    private String username;
    @SerializedName("area")
    private String area;
    @SerializedName("areaType")
    private String areaType;
    @SerializedName("plantingDate")
    private List<Integer> plantingDate;
    @SerializedName("productName")
    private String productName;
    @SerializedName("plantDateId")
    private String plantDateId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public List<Integer> getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(List<Integer> dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public List<Integer> getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(List<Integer> plantingDate) {
        this.plantingDate = plantingDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPlantDateId() {
        return plantDateId;
    }

    public void setPlantDateId(String plantDateId) {
        this.plantDateId = plantDateId;
    }
}

