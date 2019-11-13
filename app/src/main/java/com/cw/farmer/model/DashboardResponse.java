package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardResponse {

    @SerializedName("totalRecruited")
    @Expose
    private int totalRecruited;
    @SerializedName("contractsSigned")
    @Expose
    private int contractsSigned;
    @SerializedName("plantingVerified")
    @Expose
    private int plantingVerified;
    @SerializedName("plantingNotVerified")
    @Expose
    private int plantingNotVerified;
    @SerializedName("kilosHarvested")
    @Expose
    private float kilosHarvested;

    public int getTotalRecruited() {
        return totalRecruited;
    }

    public void setTotalRecruited(int totalRecruited) {
        this.totalRecruited = totalRecruited;
    }

    public DashboardResponse withTotalRecruited(int totalRecruited) {
        this.totalRecruited = totalRecruited;
        return this;
    }

    public int getContractsSigned() {
        return contractsSigned;
    }

    public void setContractsSigned(int contractsSigned) {
        this.contractsSigned = contractsSigned;
    }

    public DashboardResponse withContractsSigned(int contractsSigned) {
        this.contractsSigned = contractsSigned;
        return this;
    }

    public int getPlantingVerified() {
        return plantingVerified;
    }

    public void setPlantingVerified(int plantingVerified) {
        this.plantingVerified = plantingVerified;
    }

    public DashboardResponse withPlantingVerified(int plantingVerified) {
        this.plantingVerified = plantingVerified;
        return this;
    }

    public int getPlantingNotVerified() {
        return plantingNotVerified;
    }

    public void setPlantingNotVerified(int plantingNotVerified) {
        this.plantingNotVerified = plantingNotVerified;
    }

    public DashboardResponse withPlantingNotVerified(int plantingNotVerified) {
        this.plantingNotVerified = plantingNotVerified;
        return this;
    }

    public float getKilosHarvested() {
        return kilosHarvested;
    }

    public void setKilosHarvested(float kilosHarvested) {
        this.kilosHarvested = kilosHarvested;
    }

    public DashboardResponse withKilosHarvested(float kilosHarvested) {
        this.kilosHarvested = kilosHarvested;
        return this;
    }

}