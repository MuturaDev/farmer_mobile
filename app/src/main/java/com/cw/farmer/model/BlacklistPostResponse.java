package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlacklistPostResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("reasonId")
    @Expose
    private String reasonId;
    @SerializedName("reasondesc")
    @Expose
    private String reasondesc;
    @SerializedName("reasontype")
    @Expose
    private String reasontype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BlacklistPostResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public BlacklistPostResponse withReasonId(String reasonId) {
        this.reasonId = reasonId;
        return this;
    }

    public String getReasondesc() {
        return reasondesc;
    }

    public void setReasondesc(String reasondesc) {
        this.reasondesc = reasondesc;
    }

    public BlacklistPostResponse withReasondesc(String reasondesc) {
        this.reasondesc = reasondesc;
        return this;
    }

    public String getReasontype() {
        return reasontype;
    }

    public void setReasontype(String reasontype) {
        this.reasontype = reasontype;
    }

    public BlacklistPostResponse withReasontype(String reasontype) {
        this.reasontype = reasontype;
        return this;
    }

}