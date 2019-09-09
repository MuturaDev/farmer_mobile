package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerDocResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("docshtdesc")
    @Expose
    private String docshtdesc;
    @SerializedName("docdesc")
    @Expose
    private String docdesc;
    @SerializedName("docno")
    @Expose
    private String docno;
    @SerializedName("base64ImageDesc")
    @Expose
    private String base64ImageDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FarmerDocResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getDocshtdesc() {
        return docshtdesc;
    }

    public void setDocshtdesc(String docshtdesc) {
        this.docshtdesc = docshtdesc;
    }

    public FarmerDocResponse withDocshtdesc(String docshtdesc) {
        this.docshtdesc = docshtdesc;
        return this;
    }

    public String getDocdesc() {
        return docdesc;
    }

    public void setDocdesc(String docdesc) {
        this.docdesc = docdesc;
    }

    public FarmerDocResponse withDocdesc(String docdesc) {
        this.docdesc = docdesc;
        return this;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public FarmerDocResponse withDocno(String docno) {
        this.docno = docno;
        return this;
    }

    public String getBase64ImageDesc() {
        return base64ImageDesc;
    }

    public void setBase64ImageDesc(String base64ImageDesc) {
        this.base64ImageDesc = base64ImageDesc;
    }

    public FarmerDocResponse withBase64ImageDesc(String base64ImageDesc) {
        this.base64ImageDesc = base64ImageDesc;
        return this;
    }

}