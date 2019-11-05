package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageItemsSprayFarmer {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("farmerId")
    @Expose
    private int farmerId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("cordinates")
    @Expose
    private String cordinates;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("plantingConfirmed")
    @Expose
    private boolean plantingConfirmed;
    @SerializedName("wateringConfirmed")
    @Expose
    private boolean wateringConfirmed;
    @SerializedName("verifiedon")
    @Expose
    private List<Integer> verifiedon = null;
    @SerializedName("verifiedBy")
    @Expose
    private String verifiedBy;
    @SerializedName("contractno")
    @Expose
    private String contractno;
    @SerializedName("contractId")
    @Expose
    private int contractId;
    @SerializedName("cropDate")
    @Expose
    private List<Integer> cropDate = null;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("centreName")
    @Expose
    private String centreName;
    @SerializedName("units")
    @Expose
    private int units;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItemsSprayFarmer withId(int id) {
        this.id = id;
        return this;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public PageItemsSprayFarmer withFarmerId(int farmerId) {
        this.farmerId = farmerId;
        return this;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public PageItemsSprayFarmer withFarmerName(String farmerName) {
        this.farmerName = farmerName;
        return this;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }

    public PageItemsSprayFarmer withCordinates(String cordinates) {
        this.cordinates = cordinates;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PageItemsSprayFarmer withLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isPlantingConfirmed() {
        return plantingConfirmed;
    }

    public void setPlantingConfirmed(boolean plantingConfirmed) {
        this.plantingConfirmed = plantingConfirmed;
    }

    public PageItemsSprayFarmer withPlantingConfirmed(boolean plantingConfirmed) {
        this.plantingConfirmed = plantingConfirmed;
        return this;
    }

    public boolean isWateringConfirmed() {
        return wateringConfirmed;
    }

    public void setWateringConfirmed(boolean wateringConfirmed) {
        this.wateringConfirmed = wateringConfirmed;
    }

    public PageItemsSprayFarmer withWateringConfirmed(boolean wateringConfirmed) {
        this.wateringConfirmed = wateringConfirmed;
        return this;
    }

    public List<Integer> getVerifiedon() {
        return verifiedon;
    }

    public void setVerifiedon(List<Integer> verifiedon) {
        this.verifiedon = verifiedon;
    }

    public PageItemsSprayFarmer withVerifiedon(List<Integer> verifiedon) {
        this.verifiedon = verifiedon;
        return this;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public PageItemsSprayFarmer withVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
        return this;
    }

    public String getContractno() {
        return contractno;
    }

    public void setContractno(String contractno) {
        this.contractno = contractno;
    }

    public PageItemsSprayFarmer withContractno(String contractno) {
        this.contractno = contractno;
        return this;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public PageItemsSprayFarmer withContractId(int contractId) {
        this.contractId = contractId;
        return this;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public void setCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
    }

    public PageItemsSprayFarmer withCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
        return this;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public PageItemsSprayFarmer withAccountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public PageItemsSprayFarmer withIdno(String idno) {
        this.idno = idno;
        return this;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public PageItemsSprayFarmer withCentreName(String centreName) {
        this.centreName = centreName;
        return this;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public PageItemsSprayFarmer withUnits(int units) {
        this.units = units;
        return this;
    }

}