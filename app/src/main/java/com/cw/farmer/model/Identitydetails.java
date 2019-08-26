package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Identitydetails {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("filetype")
    @Expose
    private String filetype;
    @SerializedName("docno")
    @Expose
    private String docno;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public Identitydetails withDocId(int docId) {
        this.docId = docId;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Identitydetails withImage(String image) {
        this.image = image;
        return this;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Identitydetails withFiletype(String filetype) {
        this.filetype = filetype;
        return this;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public Identitydetails withDocno(String docno) {
        this.docno = docno;
        return this;
    }

}