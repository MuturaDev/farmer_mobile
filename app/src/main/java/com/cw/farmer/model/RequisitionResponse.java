package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequisitionResponse {

    @SerializedName("centreshtdesc")
    @Expose
    private String centreshtdesc;
    @SerializedName("centrename")
    @Expose
    private String centrename;
    @SerializedName("centreid")
    @Expose
    private int centreid;
    @SerializedName("recruitedunits")
    @Expose
    private float recruitedunits;
    @SerializedName("invitemdesc")
    @Expose
    private String invitemdesc;
    @SerializedName("quantity")
    @Expose
    private float quantity;
    @SerializedName("invtype")
    @Expose
    private String invtype;
    @SerializedName("storedesc")
    @Expose
    private String storedesc;
    @SerializedName("storequantity")
    @Expose
    private float storequantity;
    @SerializedName("remainingstock")
    @Expose
    private float remainingstock;
    @SerializedName("invId")
    @Expose
    private int invId;
    @SerializedName("reqid")
    @Expose
    private int reqid;

    public String getCentreshtdesc() {
        return centreshtdesc;
    }

    public void setCentreshtdesc(String centreshtdesc) {
        this.centreshtdesc = centreshtdesc;
    }

    public RequisitionResponse withCentreshtdesc(String centreshtdesc) {
        this.centreshtdesc = centreshtdesc;
        return this;
    }

    public String getCentrename() {
        return centrename;
    }

    public void setCentrename(String centrename) {
        this.centrename = centrename;
    }

    public RequisitionResponse withCentrename(String centrename) {
        this.centrename = centrename;
        return this;
    }

    public int getCentreid() {
        return centreid;
    }

    public void setCentreid(int centreid) {
        this.centreid = centreid;
    }

    public RequisitionResponse withCentreid(int centreid) {
        this.centreid = centreid;
        return this;
    }

    public float getRecruitedunits() {
        return recruitedunits;
    }

    public void setRecruitedunits(float recruitedunits) {
        this.recruitedunits = recruitedunits;
    }

    public RequisitionResponse withRecruitedunits(float recruitedunits) {
        this.recruitedunits = recruitedunits;
        return this;
    }

    public String getInvitemdesc() {
        return invitemdesc;
    }

    public void setInvitemdesc(String invitemdesc) {
        this.invitemdesc = invitemdesc;
    }

    public RequisitionResponse withInvitemdesc(String invitemdesc) {
        this.invitemdesc = invitemdesc;
        return this;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public RequisitionResponse withQuantity(float quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getInvtype() {
        return invtype;
    }

    public void setInvtype(String invtype) {
        this.invtype = invtype;
    }

    public RequisitionResponse withInvtype(String invtype) {
        this.invtype = invtype;
        return this;
    }

    public String getStoredesc() {
        return storedesc;
    }

    public void setStoredesc(String storedesc) {
        this.storedesc = storedesc;
    }

    public RequisitionResponse withStoredesc(String storedesc) {
        this.storedesc = storedesc;
        return this;
    }

    public float getStorequantity() {
        return storequantity;
    }

    public void setStorequantity(float storequantity) {
        this.storequantity = storequantity;
    }

    public RequisitionResponse withStorequantity(float storequantity) {
        this.storequantity = storequantity;
        return this;
    }

    public float getRemainingstock() {
        return remainingstock;
    }

    public void setRemainingstock(float remainingstock) {
        this.remainingstock = remainingstock;
    }

    public RequisitionResponse withRemainingstock(float remainingstock) {
        this.remainingstock = remainingstock;
        return this;
    }

    public int getInvId() {
        return invId;
    }

    public void setInvId(int invId) {
        this.invId = invId;
    }

    public RequisitionResponse withInvId(int invId) {
        this.invId = invId;
        return this;
    }

    public int getReqid() {
        return reqid;
    }

    public void setReqid(int reqid) {
        this.reqid = reqid;
    }

    public RequisitionResponse withReqid(int reqid) {
        this.reqid = reqid;
        return this;
    }

}

