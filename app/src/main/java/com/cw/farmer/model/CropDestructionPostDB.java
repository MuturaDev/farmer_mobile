package com.cw.farmer.model;

import com.orm.SugarRecord;

public class CropDestructionPostDB extends SugarRecord<CropDestructionPostDB> {

    public String cropDatesId;
    public String accountNumber;
    public String unit;
    public String farmers_id;
    public String file;
    public String locale;
    public String cropDestructionType;
    public String cropDestructionReasonsId;
    public String contractId;

    public CropDestructionPostDB() {
    }

    public CropDestructionPostDB(String cropDatesId, String accountNumber, String unit, String farmers_id, String file, String locale, String cropDestructionType, String cropDestructionReasonsId,String contractId) {
        this.cropDatesId = cropDatesId;
        this.accountNumber = accountNumber;
        this.unit = unit;
        this.farmers_id = farmers_id;
        this.file = file;
        this.locale = locale;
        this.cropDestructionType = cropDestructionType;
        this.cropDestructionReasonsId = cropDestructionReasonsId;
        this.contractId = contractId;

    }


}