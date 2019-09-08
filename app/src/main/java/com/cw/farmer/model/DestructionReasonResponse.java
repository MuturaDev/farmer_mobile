package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestructionReasonResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("destructionShtDesc")
    @Expose
    private String destructionShtDesc;
    @SerializedName("destructionReason")
    @Expose
    private String destructionReason;
    @SerializedName("destructionType")
    @Expose
    private String destructionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DestructionReasonResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getDestructionShtDesc() {
        return destructionShtDesc;
    }

    public void setDestructionShtDesc(String destructionShtDesc) {
        this.destructionShtDesc = destructionShtDesc;
    }

    public DestructionReasonResponse withDestructionShtDesc(String destructionShtDesc) {
        this.destructionShtDesc = destructionShtDesc;
        return this;
    }

    public String getDestructionReason() {
        return destructionReason;
    }

    public void setDestructionReason(String destructionReason) {
        this.destructionReason = destructionReason;
    }

    public DestructionReasonResponse withDestructionReason(String destructionReason) {
        this.destructionReason = destructionReason;
        return this;
    }

    public String getDestructionType() {
        return destructionType;
    }

    public void setDestructionType(String destructionType) {
        this.destructionType = destructionType;
    }

    public DestructionReasonResponse withDestructionType(String destructionType) {
        this.destructionType = destructionType;
        return this;
    }

}