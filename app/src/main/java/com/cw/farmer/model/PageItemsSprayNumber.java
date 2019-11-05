package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItemsSprayNumber {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("sprayno")
    @Expose
    private String sprayno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItemsSprayNumber withId(int id) {
        this.id = id;
        return this;
    }

    public String getSprayno() {
        return sprayno;
    }

    public void setSprayno(String sprayno) {
        this.sprayno = sprayno;
    }

    public PageItemsSprayNumber withSprayno(String sprayno) {
        this.sprayno = sprayno;
        return this;
    }

}