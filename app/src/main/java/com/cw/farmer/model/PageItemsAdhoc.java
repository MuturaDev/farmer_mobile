
package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageItemsAdhoc {

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
    @SerializedName("sendTo")
    @Expose
    private String sendTo;
    @SerializedName("createdOn")
    @Expose
    private List<Integer> createdOn = null;
    @SerializedName("approvedByUserId")
    @Expose
    private int approvedByUserId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("status")
    @Expose
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItemsAdhoc withId(int id) {
        this.id = id;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public PageItemsAdhoc withEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getCentreid() {
        return centreid;
    }

    public void setCentreid(int centreid) {
        this.centreid = centreid;
    }

    public PageItemsAdhoc withCentreid(int centreid) {
        this.centreid = centreid;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public PageItemsAdhoc withActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public PageItemsAdhoc withEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public int getRaisedByUserId() {
        return raisedByUserId;
    }

    public void setRaisedByUserId(int raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
    }

    public PageItemsAdhoc withRaisedByUserId(int raisedByUserId) {
        this.raisedByUserId = raisedByUserId;
        return this;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public PageItemsAdhoc withRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
        return this;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public PageItemsAdhoc withSendTo(String sendTo) {
        this.sendTo = sendTo;
        return this;
    }

    public List<Integer> getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(List<Integer> createdOn) {
        this.createdOn = createdOn;
    }

    public PageItemsAdhoc withCreatedOn(List<Integer> createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public int getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(int approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public PageItemsAdhoc withApprovedByUserId(int approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PageItemsAdhoc withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageItemsAdhoc withStatus(String status) {
        this.status = status;
        return this;
    }

}