package com.cw.farmer.spinner_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class GrowthParameterResponse implements  Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("paramName")
    @Expose
    private String paramName;
    @SerializedName("paramType")
    @Expose
    private String paramType;
    @SerializedName("paramCateogry")
    @Expose
    private String paramCateogry;
    @SerializedName("paramConstraints")
    @Expose
    private String paramConstraints;
    @SerializedName("hierarchy")
    @Expose
    private String hierarchy;
    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("parentName")
    @Expose
    private String parentName;
    @SerializedName("parentobject")
    @Expose
    private String parentobject;
    @SerializedName("paramStatus")
    @Expose
    private String paramStatus;
    @SerializedName("parentType")
    @Expose
    private String parentType;
    @SerializedName("additionalDependentValue")
    @Expose
    private String additionalDependentValue;
    @SerializedName("additionalDependParamType")
    @Expose
    private String additionalDependParamType;
    @SerializedName("additionalDependConstraints")
    @Expose
    private String additionalDependConstraints;
    @SerializedName("parameters")
    @Expose
    private List<Object> parameters = null;


    @Override
    public String toString() {
        return paramName;
    }

    public GrowthParameterResponse(Integer id, String paramName, String paramType, String paramCateogry, String paramConstraints, String hierarchy, Integer parentId, String parentName, String parentobject, String paramStatus, String parentType, String additionalDependentValue, String additionalDependParamType, String additionalDependConstraints, List<Object> parameters) {
        this.id = id;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramCateogry = paramCateogry;
        this.paramConstraints = paramConstraints;
        this.hierarchy = hierarchy;
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentobject = parentobject;
        this.paramStatus = paramStatus;
        this.parentType = parentType;
        this.additionalDependentValue = additionalDependentValue;
        this.additionalDependParamType = additionalDependParamType;
        this.additionalDependConstraints = additionalDependConstraints;
        this.parameters = parameters;
    }

    public Integer getId() {
        return id;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParamCateogry() {
        return paramCateogry;
    }

    public String getParamConstraints() {
        return paramConstraints;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentobject() {
        return parentobject;
    }

    public String getParamStatus() {
        return paramStatus;
    }

    public String getParentType() {
        return parentType;
    }

    public String getAdditionalDependentValue() {
        return additionalDependentValue;
    }

    public String getAdditionalDependParamType() {
        return additionalDependParamType;
    }

    public String getAdditionalDependConstraints() {
        return additionalDependConstraints;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
