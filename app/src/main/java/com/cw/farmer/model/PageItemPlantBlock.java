package com.cw.farmer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PageItemPlantBlock implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("blockname")
    private String blockname;
    @SerializedName("farmNameId")
    private String farmNameId;
    @SerializedName("farmName")
    private String farmName;
    @SerializedName("dateCreated")
    private List<Integer> dateCreated;
    @SerializedName("userId")
    private String userId;
    @SerializedName("username")
    private String username;
    @SerializedName("centerId")
    private String centerId;
    @SerializedName("centerName")
    private String centerName;


    public int getId() {
        return id;
    }

    public String getBlockname() {
        return blockname;
    }

    public String getFarmNameId() {
        return farmNameId;
    }

    public String getFarmName() {
        return farmName;
    }

    public List<Integer> getDateCreated() {
        return dateCreated;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getCenterName() {
        return centerName;
    }
}

