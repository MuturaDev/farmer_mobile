package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PlantingVerificationResponse {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("farmerId")
    @Expose
    int farmerId;
    @SerializedName("farmerName")
    @Expose
    String farmerName;
    @SerializedName("cordinates")
    @Expose
    String cordinates;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("plantingConfirmed")
    @Expose
    String plantingConfirmed;
    @SerializedName("wateringConfirmed")
    @Expose
    String wateringConfirmed;
    @SerializedName("verifiedon")
    @Expose
    List<Integer> verifiedOn;
    @SerializedName("verifiedBy")
    @Expose
    String verifiedBy;
    @SerializedName("contractno")
    @Expose
    String contractNo;
    @SerializedName("contractId")
    @Expose
    String contractId;
    @SerializedName("cropDate")
    @Expose
    List<Integer> cropDate;
    @SerializedName("accountNo")
    @Expose
    String accountNo;
    @SerializedName("idno")
    @Expose
    String idno;
    @SerializedName("centreName")
    @Expose
    String centreName;
    @SerializedName("units")
    @Expose
    String units;
    @SerializedName("contractUnits")
    @Expose
    String contractUnits;


    public PlantingVerificationResponse(int id, int farmerId, String farmerName, String cordinates, String location, String plantingConfirmed, String wateringConfirmed, List<Integer> verifiedOn, String verifiedBy, String contractNo, String contractId, List<Integer> cropDate, String accountNo, String idno, String centreName, String units, String contractUnits) {
        this.id = id;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.cordinates = cordinates;
        this.location = location;
        this.plantingConfirmed = plantingConfirmed;
        this.wateringConfirmed = wateringConfirmed;
        this.verifiedOn = verifiedOn;
        this.verifiedBy = verifiedBy;
        this.contractNo = contractNo;
        this.contractId = contractId;
        this.cropDate = cropDate;
        this.accountNo = accountNo;
        this.idno = idno;
        this.centreName = centreName;
        this.units = units;
        this.contractUnits = contractUnits;
    }

    public int getId() {
        return id;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getCordinates() {
        return cordinates;
    }

    public String getLocation() {
        return location;
    }

    public String getPlantingConfirmed() {
        return plantingConfirmed;
    }

    public String getWateringConfirmed() {
        return wateringConfirmed;
    }

    public List<Integer> getVerifiedOn() {
        return verifiedOn;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public String getContractNo() {
        return contractNo;
    }

    public String getContractId() {
        return contractId;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getIdno() {
        return idno;
    }

    public String getCentreName() {
        return centreName;
    }

    public String getUnits() {
        return units;
    }

    public String getContractUnits() {
        return contractUnits;
    }

    @Override
    public String toString() {
        return "PlantingVerificationResponse{" +
                "id=" + id +
                ", farmerId=" + farmerId +
                ", farmerName='" + farmerName + '\'' +
                ", cordinates='" + cordinates + '\'' +
                ", location='" + location + '\'' +
                ", plantingConfirmed='" + plantingConfirmed + '\'' +
                ", wateringConfirmed='" + wateringConfirmed + '\'' +
                ", verifiedOn=" + verifiedOn +
                ", verifiedBy='" + verifiedBy + '\'' +
                ", contractNo='" + contractNo + '\'' +
                ", contractId='" + contractId + '\'' +
                ", cropDate=" + cropDate +
                ", accountNo='" + accountNo + '\'' +
                ", idno='" + idno + '\'' +
                ", centreName='" + centreName + '\'' +
                ", units='" + units + '\'' +
                ", contractUnits='" + contractUnits + '\'' +
                '}';
    }
}
