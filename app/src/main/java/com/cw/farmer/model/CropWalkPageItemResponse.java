package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CropWalkPageItemResponse implements Serializable {

//    @SerializedName("id")
//    @Expose
//    private Integer ID;
//    @SerializedName("farmerId")
//    @Expose
//    private Integer farmerId;
//    @SerializedName("farmerName")
//    @Expose
//    private String farmerName;
//    @SerializedName("cropDateId")
//    @Expose
//    private Integer cropDateId;
//    @SerializedName("cropDate")
//    @Expose
//    private List<Integer> cropDate = null;
//    @SerializedName("centreName")
//    @Expose
//    private String centreName;
//    @SerializedName("parameterName")
//    @Expose
//    private String parameterName;
//    @SerializedName("parameterValue")
//    @Expose
//    private String parameterValue;
//    @SerializedName("parameterId")
//    @Expose
//    private Integer parameterId;
//    @SerializedName("units")
//    @Expose
//    private int units;
//    @SerializedName("doneBy")
//    @Expose
//    private String doneBy;
//    @SerializedName("doneDate")
//    @Expose
//    private List<Integer> doneDate = null;


    @SerializedName("farmerId")
    @Expose
    private Integer farmerId;
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
    private Integer totalUnits;
    @SerializedName("plantingId")
    @Expose
    private Integer plantingId;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
        @SerializedName("centreName")
    @Expose
    private String centreName;


    public CropWalkPageItemResponse(Integer farmerId, String farmerName, String farmerCode, List<Integer> cropDate, Integer totalUnits, Integer plantingId, String idno, String mobileno, String centreName) {
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.farmerCode = farmerCode;
        this.cropDate = cropDate;
        this.totalUnits = totalUnits;
        this.plantingId = plantingId;
        this.idno = idno;
        this.mobileno = mobileno;
        this.centreName = centreName;
    }

    public String getCentreName() {
        return centreName;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public Integer getTotalUnits() {
        return totalUnits;
    }

    public Integer getPlantingId() {
        return plantingId;
    }

    public String getIdno() {
        return idno;
    }

    public String getMobileno() {
        return mobileno;
    }
}
