package com.cw.farmer.model;

import com.orm.SugarRecord;

public class CropDestructionDB extends SugarRecord<CropDestructionDB> {

    public int id_reason;
    public String destructionShtDesc;
    public String destructionReason;
    public String destructionType;
    public String file;
    public String dateFormat;
    public String locale;

    public CropDestructionDB() {
    }

    public CropDestructionDB(int id_reason, String destructionShtDesc, String destructionReason, String destructionType) {
        this.id_reason = id_reason;
        this.destructionShtDesc = destructionShtDesc;
        this.destructionReason = destructionReason;
        this.destructionType = destructionType;
    }


}