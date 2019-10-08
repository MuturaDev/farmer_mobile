package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItemsPlantVerify {

    @SerializedName("farmerId")
    @Expose
    private int farmerId;
    @SerializedName("contractId")
    @Expose
    private int contractId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("farmerIdNo")
    @Expose
    private String farmerIdNo;
    @SerializedName("centreName")
    @Expose
    private String centreName;
    @SerializedName("plantingId")
    @Expose
    private int plantingId;
    @SerializedName("plantingDate")
    @Expose
    private List<Integer> plantingDate = null;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("nounits")
    @Expose
    private int nounits;

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public PageItemsPlantVerify withFarmerId(int farmerId) {
        this.farmerId = farmerId;
        return this;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public PageItemsPlantVerify withContractId(int contractId) {
        this.contractId = contractId;
        return this;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public PageItemsPlantVerify withFarmerName(String farmerName) {
        this.farmerName = farmerName;
        return this;
    }

    public String getFarmerIdNo() {
        return farmerIdNo;
    }

    public void setFarmerIdNo(String farmerIdNo) {
        this.farmerIdNo = farmerIdNo;
    }

    public PageItemsPlantVerify withFarmerIdNo(String farmerIdNo) {
        this.farmerIdNo = farmerIdNo;
        return this;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public PageItemsPlantVerify withCentreName(String centreName) {
        this.centreName = centreName;
        return this;
    }

    public int getPlantingId() {
        return plantingId;
    }

    public void setPlantingId(int plantingId) {
        this.plantingId = plantingId;
    }

    public PageItemsPlantVerify withPlantingId(int plantingId) {
        this.plantingId = plantingId;
        return this;
    }

    public List<Integer> getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(List<Integer> plantingDate) {
        this.plantingDate = plantingDate;
    }

    public PageItemsPlantVerify withPlantingDate(List<Integer> plantingDate) {
        this.plantingDate = plantingDate;
        return this;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public PageItemsPlantVerify withContractNo(String contractNo) {
        this.contractNo = contractNo;
        return this;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public PageItemsPlantVerify withAccountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public int getNounits() {
        return nounits;
    }

    public void setNounits(int nounits) {
        this.nounits = nounits;
    }

    public PageItemsPlantVerify withNounits(int nounits) {
        this.nounits = nounits;
        return this;
    }

}