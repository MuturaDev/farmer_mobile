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

    public ContractSignDB() {
    }

    public ContractSignDB(String referenceNo, String cropDateId, String units, String farmerId, String file, String dateFormat, String locale) {
        this.referenceNo = referenceNo;
        this.cropDateId = cropDateId;
        this.units = units;
        this.farmerId = farmerId;
        this.file = file;
        this.dateFormat = dateFormat;
        this.locale = locale;
    }


}