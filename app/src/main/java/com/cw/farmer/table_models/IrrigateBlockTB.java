package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class IrrigateBlockTB extends SugarRecord<IrrigateBlockTB> {

    private String blockId;
    private String irrigationHours;
    private String cubicLitres;
    private String locale;

    public IrrigateBlockTB(String blockId, String irrigationHours, String cubicLitres, String locale) {
        this.blockId = blockId;
        this.irrigationHours = irrigationHours;
        this.cubicLitres = cubicLitres;
        this.locale = locale;
    }

    public IrrigateBlockTB() {
    }

    public String getBlockId() {
        return blockId;
    }

    public String getIrrigationHours() {
        return irrigationHours;
    }

    public String getCubicLitres() {
        return cubicLitres;
    }

    public String getLocale() {
        return locale;
    }
}
