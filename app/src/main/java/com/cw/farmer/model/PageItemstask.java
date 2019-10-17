package com.cw.farmer.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItemstask {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("entityId")
    @Expose
    private int entityId;
    @SerializedName("centreid")
    @Expose
    private int centreid;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("entityName")
    @Expose
    private String entityName;
    @SerializedName("raisedByUserId")
    @Expose
    private int raisedByUserId;
    @SerializedName("raisedBy")
    @Expose
    private String raisedBy;
    @SerializedName("createdOn")
    @Expose
    private List<Integer> createdOn = null;
    @SerializedName("approvedByUserId")
    @Expose
    private int approvedByUserId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("centrename")
    @Expose
    private String centrename;
    @SerializedName("cropDate")
    @Expose
    private List<Integer> cropDate = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItemstask withId(int id) {
        this.id = id;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public PageItemstask withEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getCentreid() {
        return centreid;
    }

    public void setCentreid(int centreid) {
        this.centreid = centreid;
    }

    public PageItemstask withCentreid(int centreid) {
        this.centreid = centreid;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public PageItemstask withActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public PageItemstask withEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public int getRaisedByUserId() {
        return raisedByUserId;
    }

    public void setRaisedByUserId(int raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
    }

    public PageItemstask withRaisedByUserId(int raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
        return this;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public PageItemstask withRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
        return this;
    }

    public List<Integer> getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(List<Integer> createdOn) {
        this.createdOn = createdOn;
    }

    public PageItemstask withCreatedOn(List<Integer> createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public int getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(int approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public PageItemstask withApprovedByUserId(int approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageItemstask withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCentrename() {
        return centrename;
    }

    public void setCentrename(String centrename) {
        this.centrename = centrename;
    }

    public PageItemstask withCentrename(String centrename) {
        this.centrename = centrename;
        return this;
    }

    public List<Integer> getCropDate() {
        return cropDate;
    }

    public void setCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
    }

    public PageItemstask withCropDate(List<Integer> cropDate) {
        this.cropDate = cropDate;
        return this;
    }
}