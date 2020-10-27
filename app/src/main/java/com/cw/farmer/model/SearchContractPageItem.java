package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchContractPageItem {

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
    @SerializedName("recruitid")
    @Expose
    private int recruitid;

    @SerializedName("idno")
    @Expose
    private String idno;

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public SearchContractPageItem withFarmerId(int farmerId) {
        this.farmerId = farmerId;
        return this;
    }

    public int getRecruitId() {
        return recruitid;
    }

    public void setRecruitId(int recruitId) {
        this.recruitid = recruitid;
    }

    public SearchContractPageItem withRecruitId(int recruitid) {
        this.recruitid = recruitid;
        return this;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public SearchContractPageItem withFarmerName(String farmerName) {
        this.farmerName = farmerName;
        return this;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public SearchContractPageItem withFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
        return this;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public void setCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
    }

    public SearchContractPageItem withCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
        return this;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public SearchContractPageItem withTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
        return this;
    }

    public int getPlantingId() {
        return plantingId;
    }

    public void setPlantingId(int plantingId) {
        this.plantingId = plantingId;
    }

    public SearchContractPageItem withPlantingId(int plantingId) {
        this.plantingId = plantingId;
        return this;
    }
}

