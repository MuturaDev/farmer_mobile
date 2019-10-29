package com.cw.farmer.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CentreRequiste {

    @SerializedName("centrestores")
    @Expose
    private List<Centrestore> centrestores = null;

    public List<Centrestore> getCentrestores() {
        return centrestores;
    }

    public void setCentrestores(List<Centrestore> centrestores) {
        this.centrestores = centrestores;
    }

    public CentreRequiste withCentrestores(List<Centrestore> centrestores) {
        this.centrestores = centrestores;
        return this;
    }

}