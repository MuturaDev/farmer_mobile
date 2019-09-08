package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItemsDestruction {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("referenceNo")
    @Expose
    private String referenceNo;
    @SerializedName("cropDate")
    @Expose
    private List<Integer> cropDate = null;
    @SerializedName("units")
    @Expose
    private int units;
    @SerializedName("fileId")
    @Expose
    private int fileId;
    @SerializedName("famerName")
    @Expose
    private String famerName;
    @SerializedName("farmerCode")
    @Expose
    private String farmerCode;
    @SerializedName("farmerId")
    @Expose
    private int farmerId;
    @SerializedName("cropDateId")
    @Expose
    private int cropDateId;
    @SerializedName("contractDate")
    @Expose
    private List<Integer> contractDate = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItemsDestruction withId(int id) {
        this.id = id;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public PageItemsDestruction withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public PageItemsDestruction withReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
        return this;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public void setCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
    }

    public PageItemsDestruction withCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
        return this;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public PageItemsDestruction withUnits(int units) {
        this.units = units;
        return this;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public PageItemsDestruction withFileId(int fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getFamerName() {
        return famerName;
    }

    public void setFamerName(String famerName) {
        this.famerName = famerName;
    }

    public PageItemsDestruction withFamerName(String famerName) {
        this.famerName = famerName;
        return this;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public PageItemsDestruction withFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
        return this;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public PageItemsDestruction withFarmerId(int farmerId) {
        this.farmerId = farmerId;
        return this;
    }

    public int getCropDateId() {
        return cropDateId;
    }

    public void setCropDateId(int cropDateId) {
        this.cropDateId = cropDateId;
    }

    public PageItemsDestruction withCropDateId(int cropDateId) {
        this.cropDateId = cropDateId;
        return this;
    }

    public List<Integer> getContractDate() {
        return contractDate;
    }

    public void setContractDate(List<Integer> contractDate) {
        this.contractDate = contractDate;
    }

    public PageItemsDestruction withContractDate(List<Integer> contractDate) {
        this.contractDate = contractDate;
        return this;
    }

}