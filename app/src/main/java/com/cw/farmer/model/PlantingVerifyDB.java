package com.cw.farmer.model;

import com.orm.SugarRecord;

public class PlantingVerifyDB extends SugarRecord<PlantingVerifyDB> {

    public String cordinates;
    public String location;
    public String contractid;
    public String plant_value;
    public String water_value;

    public PlantingVerifyDB() {
    }

    public PlantingVerifyDB(String cordinates, String location, String contractid, String plant_value, String water_value) {
        this.cordinates = cordinates;
        this.location = location;
        this.contractid = contractid;
        this.plant_value = plant_value;
        this.water_value = water_value;
    }


}