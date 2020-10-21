package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class PlantBlockTB extends SugarRecord<PlantBlockTB> {

    private String bagsPlanted;
    private String seedRate;
    private String varietyId;
    private String blockId;
    private String cordinates;
    private String location;
    private String bagLotNo;

    public PlantBlockTB(String bagsPlanted, String seedRate, String varietyId, String blockId, String cordinates, String location, String bagLotNo) {
        this.bagsPlanted = bagsPlanted;
        this.seedRate = seedRate;
        this.varietyId = varietyId;
        this.blockId = blockId;
        this.cordinates = cordinates;
        this.location = location;
        this.bagLotNo = bagLotNo;
    }

    public PlantBlockTB() {
    }

    public String getBagsPlanted() {
        return bagsPlanted;
    }

    public String getSeedRate() {
        return seedRate;
    }

    public String getVarietyId() {
        return varietyId;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getCordinates() {
        return cordinates;
    }

    public String getLocation() {
        return location;
    }

    public String getBagLotNo() {
        return bagLotNo;
    }
}
