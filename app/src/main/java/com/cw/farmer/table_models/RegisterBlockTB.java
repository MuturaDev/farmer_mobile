package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class RegisterBlockTB extends SugarRecord<RegisterBlockTB> {

    private String centerID;
    private String farmNameId;
    private String blockName;

    public RegisterBlockTB(String centerID, String farmNameId, String blockName) {
        this.centerID = centerID;
        this.farmNameId = farmNameId;
        this.blockName = blockName;
    }

    public RegisterBlockTB() {
    }

    public String getCenterID() {
        return centerID;
    }

    public String getFarmNameId() {
        return farmNameId;
    }

    public String getBlockName() {
        return blockName;
    }
}
