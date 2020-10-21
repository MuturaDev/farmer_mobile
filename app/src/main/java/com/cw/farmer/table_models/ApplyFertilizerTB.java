package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class ApplyFertilizerTB extends SugarRecord<ApplyFertilizerTB> {

    private String blockId;
    private String fertilizerId;
    private String appliedRate;
    private String method;
    private String equipment;
    private String locale;

    public ApplyFertilizerTB(String blockId, String fertilizerId, String appliedRate, String method, String equipment, String locale) {
        this.blockId = blockId;
        this.fertilizerId = fertilizerId;
        this.appliedRate = appliedRate;
        this.method = method;
        this.equipment = equipment;
        this.locale = locale;
    }

    public ApplyFertilizerTB() {
    }

    public String getBlockId() {
        return blockId;
    }

    public String getFertilizerId() {
        return fertilizerId;
    }

    public String getAppliedRate() {
        return appliedRate;
    }

    public String getMethod() {
        return method;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getLocale() {
        return locale;
    }
}
