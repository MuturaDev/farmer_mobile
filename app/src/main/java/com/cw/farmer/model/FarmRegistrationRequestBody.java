package com.cw.farmer.model;

public class FarmRegistrationRequestBody {


    private int centerId;
    private int farmNameId;
    private String blockName;


    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public int getFarmNameId() {
        return farmNameId;
    }

    public void setFarmNameId(int farmNameId) {
        this.farmNameId = farmNameId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}
