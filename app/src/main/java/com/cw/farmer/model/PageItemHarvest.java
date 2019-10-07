package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItemHarvest {

    @SerializedName("farmerId")
    @Expose
    private int farmerId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("farmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("cropDate")
    @Expose
    private List<Integer> cropDate = null;
    @SerializedName("totalUnits")
    @Expose
    private int totalUnits;
    @SerializedName("plantingId")
    @Expose
    private int plantingId;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("centreName")
    @Expose
    private String centreName;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public PageItemHarvest withFarmerId(int farmerId) {
        this.farmerId = farmerId;
        return this;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public PageItemHarvest withFarmerName(String farmerName) {
        this.farmerName = farmerName;
        return this;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public PageItemHarvest withFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
        return this;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public void setCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
    }

    public PageItemHarvest withCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
        return this;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public PageItemHarvest withTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
        return this;
    }

    public int getPlantingId() {
        return plantingId;
    }

    public void setPlantingId(int plantingId) {
        this.plantingId = plantingId;
    }

    public PageItemHarvest withPlantingId(int plantingId) {
        this.plantingId = plantingId;
        return this;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public PageItemHarvest withIdno(String idno) {
        this.idno = idno;
        return this;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public PageItemHarvest withCentreName(String centreName) {
        this.centreName = centreName;
        return this;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public PageItemHarvest withMobileno(String mobileno) {
        this.mobileno = mobileno;
        return this;
    }

}