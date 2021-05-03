package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class CropWalkTB extends SugarRecord<CropWalkTB> {
    @SerializedName("farmerid ")
    @Expose
    private Integer farmerid;

    @SerializedName("dateid")
    @Expose
    private Integer dateid;

    @SerializedName("walkid")
    @Expose
    private Integer walkid;

    @SerializedName("cordinates")
    @Expose
    private String cordinates;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("cropstageId")
    @Expose
    private Integer cropstageId;

    @SerializedName("centerid")
    @Expose
    private Integer centerid;

    @SerializedName("locale")
    @Expose
    private String locale;

    @SerializedName("units")
    @Expose
    private Integer units;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("filetype")
    @Expose
    private String filetype;

    @SerializedName("other")
    @Expose
    private String other;

    @SerializedName("comment")
    @Expose
    private String comment;


    public CropWalkTB() {
    }

    public CropWalkTB(Integer farmerid, Integer dateid, Integer walkid, String cordinates, String location, Integer cropstageId, Integer centerid, String locale, Integer units, String image, String filetype, String other, String comment) {
        this.farmerid = farmerid;
        this.dateid = dateid;
        this.walkid = walkid;
        this.cordinates = cordinates;
        this.location = location;
        this.cropstageId = cropstageId;
        this.centerid = centerid;
        this.locale = locale;
        this.units = units;
        this.image = image;
        this.filetype = filetype;
        this.other = other;
        this.comment = comment;
    }

    public String getOther() {
        return other;
    }

    public String getComment() {
        return comment;
    }

    public Integer getFarmerid() {
        return farmerid;
    }

    public Integer getDateid() {
        return dateid;
    }

    public Integer getWalkid() {
        return walkid;
    }

    public String getCordinates() {
        return cordinates;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCropstageId() {
        return cropstageId;
    }

    public Integer getCenterid() {
        return centerid;
    }

    public String getLocale() {
        return locale;
    }

    public Integer getUnits() {
        return units;
    }

    public String getImage() {
        return image;
    }

    public String getFiletype() {
        return filetype;
    }
}
