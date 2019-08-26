package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CropDateResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("prodId")
    @Expose
    private String prodId;
    @SerializedName("prodname")
    @Expose
    private String prodname;
    @SerializedName("plantingDate")
    @Expose
    private List<Integer> plantingDate = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CropDateResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public CropDateResponse withProdId(String prodId) {
        this.prodId = prodId;
        return this;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public CropDateResponse withProdname(String prodname) {
        this.prodname = prodname;
        return this;
    }

    public List<Integer> getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(List<Integer> plantingDate) {
        this.plantingDate = plantingDate;
    }

    public CropDateResponse withPlantingDate(List<Integer> plantingDate) {
        this.plantingDate = plantingDate;
        return this;
    }

}