package com.cw.farmer.model;

import com.orm.SugarRecord;

public class CropDateDB extends SugarRecord<CropDateDB> {

    public int id_crop;
    public String prodId;
    public String prodname;
    public String plantingDate;

    public CropDateDB() {
    }

    public CropDateDB(int id_crop, String prodId, String prodname, String plantingDate) {
        this.id_crop = id_crop;
        this.prodId = prodId;
        this.prodname = prodname;
        this.plantingDate = plantingDate;
    }


}