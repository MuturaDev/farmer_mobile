package com.cw.farmer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PageItemHarvestBlocks implements Serializable {


    @SerializedName("id")
    private int id;
    @SerializedName("seedrate")
    private String seedrate;
    @SerializedName("bagsplanted")
    private int bagsplanted;
    @SerializedName("baglotno")
    private int baglotno;
    @SerializedName("cordinates")
    private String cordinates;
    @SerializedName("location")
    private String location;
    @SerializedName("createdon")
    private List<Integer> createdon;
    @SerializedName("createdby")
    private String createdby;
    @SerializedName("blockName")
    private String blockName;
    @SerializedName("varietyName")
    private String varietyName;

    public int getId() {
        return id;
    }

    public String getSeedrate() {
        return seedrate;
    }

    public int getBagsplanted() {
        return bagsplanted;
    }

    public int getBaglotno() {
        return baglotno;
    }

    public String getCordinates() {
        return cordinates;
    }

    public String getLocation() {
        return location;
    }

    public List<Integer> getCreatedon() {
        return createdon;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getVarietyName() {
        return varietyName;
    }
}

