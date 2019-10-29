package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCentreResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shtdesc")
    @Expose
    private String shtdesc;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("hierarchy")
    @Expose
    private String hierarchy;
    @SerializedName("parentId")
    @Expose
    private int parentId;
    @SerializedName("parentName")
    @Expose
    private String parentName;
    @SerializedName("parentobject")
    @Expose
    private String parentobject;
    @SerializedName("centreObjectId")
    @Expose
    private int centreObjectId;
    @SerializedName("centretype")
    @Expose
    private String centretype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AllCentreResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AllCentreResponse withName(String name) {
        this.name = name;
        return this;
    }

    public String getShtdesc() {
        return shtdesc;
    }

    public void setShtdesc(String shtdesc) {
        this.shtdesc = shtdesc;
    }

    public AllCentreResponse withShtdesc(String shtdesc) {
        this.shtdesc = shtdesc;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AllCentreResponse withType(String type) {
        this.type = type;
        return this;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public AllCentreResponse withHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
        return this;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public AllCentreResponse withParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public AllCentreResponse withParentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public String getParentobject() {
        return parentobject;
    }

    public void setParentobject(String parentobject) {
        this.parentobject = parentobject;
    }

    public AllCentreResponse withParentobject(String parentobject) {
        this.parentobject = parentobject;
        return this;
    }

    public int getCentreObjectId() {
        return centreObjectId;
    }

    public void setCentreObjectId(int centreObjectId) {
        this.centreObjectId = centreObjectId;
    }

    public AllCentreResponse withCentreObjectId(int centreObjectId) {
        this.centreObjectId = centreObjectId;
        return this;
    }

    public String getCentretype() {
        return centretype;
    }

    public void setCentretype(String centretype) {
        this.centretype = centretype;
    }

    public AllCentreResponse withCentretype(String centretype) {
        this.centretype = centretype;
        return this;
    }
}