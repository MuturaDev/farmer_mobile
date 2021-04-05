package com.cw.farmer.spinner_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class CropStageResponse implements Serializable {
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
    @SerializedName("paramStatus")
    @Expose
    private String paramStatus;
    @SerializedName("parentType")
    @Expose
    private String parentType;
    @SerializedName("parameters")
    @Expose
    private List<GrowthParameterResponse> parameters = null;


    @Override
    public String toString() {
        return paramName;
    }

    public CropStageResponse(Integer id, String paramName, String paramType, String paramCateogry, String paramConstraints, String hierarchy, String paramStatus, String parentType, List<GrowthParameterResponse> parameters) {
        this.id = id;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramCateogry = paramCateogry;
        this.paramConstraints = paramConstraints;
        this.hierarchy = hierarchy;
        this.paramStatus = paramStatus;
        this.parentType = parentType;
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

    public String getParamStatus() {
        return paramStatus;
    }

    public String getParentType() {
        return parentType;
    }

    public List<GrowthParameterResponse> getParameters() {
        return parameters;
    }
}
