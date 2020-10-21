package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class HarvestBlockTB extends SugarRecord<HarvestBlockTB> {

    private String harvestKilos;
    private String blockId;
    private String locale;


    public HarvestBlockTB(String harvestKilos, String blockId, String locale) {
        this.harvestKilos = harvestKilos;
        this.blockId = blockId;
        this.locale = locale;
    }

    public HarvestBlockTB() {
    }

    public String getHarvestKilos() {
        return harvestKilos;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getLocale() {
        return locale;
    }
}
