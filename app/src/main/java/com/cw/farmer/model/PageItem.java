package com.cw.farmer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageItem  implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dateOfBirth")
    @Expose
    private List<Integer> dateOfBirth = null;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("activated")
    @Expose
    private String activated;
    @SerializedName("centername")
    @Expose
    private String centername;
    @SerializedName("dateRegistered")
    @Expose
    private List<Integer> dateRegistered = null;
    @SerializedName("farmercode")
    @Expose
    private String farmercode;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("datatables")
    @Expose
    private List<Object> datatables = null;
    @SerializedName("farmerRejectReasons")
    @Expose
    private List<Object> farmerRejectReasons = null;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("imageId")
    @Expose
    private int imageId;
    @SerializedName("imagePresent")
    @Expose
    private boolean imagePresent;

    protected PageItem(Parcel in) {
        id = in.readInt();
        firstname = in.readString();
        middlename = in.readString();
        lastname = in.readString();
        mobileno = in.readString();
        email = in.readString();
        gender = in.readString();
        address = in.readString();
        activated = in.readString();
        centername = in.readString();
        farmercode = in.readString();
        idno = in.readString();
        imageId = in.readInt();
        imagePresent = in.readByte() != 0;
    }

    public static final Creator<PageItem> CREATOR = new Creator<PageItem>() {
        @Override
        public PageItem createFromParcel(Parcel in) {
            return new PageItem(in);
        }

        @Override
        public PageItem[] newArray(int size) {
            return new PageItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PageItem withId(int id) {
        this.id = id;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public PageItem withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public PageItem withMiddlename(String middlename) {
        this.middlename = middlename;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public PageItem withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public PageItem withMobileno(String mobileno) {
        this.mobileno = mobileno;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PageItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public PageItem withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public List<Integer> getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(List<Integer> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PageItem withDateOfBirth(List<Integer> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PageItem withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public PageItem withActivated(String activated) {
        this.activated = activated;
        return this;
    }

    public String getCentername() {
        return centername;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }

    public PageItem withCentername(String centername) {
        this.centername = centername;
        return this;
    }

    public List<Integer> getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(List<Integer> dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public PageItem withDateRegistered(List<Integer> dateRegistered) {
        this.dateRegistered = dateRegistered;
        return this;
    }

    public String getFarmercode() {
        return farmercode;
    }

    public void setFarmercode(String farmercode) {
        this.farmercode = farmercode;
    }

    public PageItem withFarmercode(String farmercode) {
        this.farmercode = farmercode;
        return this;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public PageItem withIdno(String idno) {
        this.idno = idno;
        return this;
    }

    public List<Object> getDatatables() {
        return datatables;
    }

    public void setDatatables(List<Object> datatables) {
        this.datatables = datatables;
    }

    public PageItem withDatatables(List<Object> datatables) {
        this.datatables = datatables;
        return this;
    }

    public List<Object> getFarmerRejectReasons() {
        return farmerRejectReasons;
    }

    public void setFarmerRejectReasons(List<Object> farmerRejectReasons) {
        this.farmerRejectReasons = farmerRejectReasons;
    }

    public PageItem withFarmerRejectReasons(List<Object> farmerRejectReasons) {
        this.farmerRejectReasons = farmerRejectReasons;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PageItem withStatus(Status status) {
        this.status = status;
        return this;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public PageItem withImageId(int imageId) {
        this.imageId = imageId;
        return this;
    }

    public boolean isImagePresent() {
        return imagePresent;
    }

    public void setImagePresent(boolean imagePresent) {
        this.imagePresent = imagePresent;
    }

    public PageItem withImagePresent(boolean imagePresent) {
        this.imagePresent = imagePresent;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(firstname);
        parcel.writeString(middlename);
        parcel.writeString(lastname);
        parcel.writeString(mobileno);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeString(address);
        parcel.writeString(activated);
        parcel.writeString(centername);
        parcel.writeString(farmercode);
        parcel.writeString(idno);
        parcel.writeInt(imageId);
        parcel.writeByte((byte) (imagePresent ? 1 : 0));
    }
}