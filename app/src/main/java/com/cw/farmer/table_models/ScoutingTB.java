package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class ScoutingTB extends SugarRecord<ScoutingTB> {

    private String blockId;
    private String germination;
    private String weeded;
    private String watered;
    private String survivalRate;
    private String floweringRate;
    private String averagePods;
    private String locale;

    public ScoutingTB(String blockId, String germination, String weeded, String watered, String survivalRate, String floweringRate, String averagePods, String locale) {
        this.blockId = blockId;
        this.germination = germination;
        this.weeded = weeded;
        this.watered = watered;
        this.survivalRate = survivalRate;
        this.floweringRate = floweringRate;
        this.averagePods = averagePods;
        this.locale = locale;
    }


    public ScoutingTB() {
    }


    public String getBlockId() {
        return blockId;
    }

    public String getGermination() {
        return germination;
    }

    public String getWeeded() {
        return weeded;
    }

    public String getWatered() {
        return watered;
    }

    public String getSurvivalRate() {
        return survivalRate;
    }

    public String getFloweringRate() {
        return floweringRate;
    }

    public String getAveragePods() {
        return averagePods;
    }

    public String getLocale() {
        return locale;
    }
}
