package com.cw.farmer.model;

import com.orm.SugarRecord;

public class ContractSignDB extends SugarRecord<ContractSignDB> {

    public String referenceNo;
    public String cropDateId;
    public String units;
    public String farmerId;
    public String file;
    public String dateFormat;
    public String locale;
    public String recruitId;

    public ContractSignDB() {
    }

    public ContractSignDB(String referenceNo, String cropDateId, String units, String farmerId, String file, String dateFormat, String locale, String recruitId) {
        this.referenceNo = referenceNo;
        this.cropDateId = cropDateId;
        this.units = units;
        this.farmerId = farmerId;
        this.file = file;
        this.dateFormat = dateFormat;
        this.locale = locale;
        this.recruitId = recruitId;
    }


}