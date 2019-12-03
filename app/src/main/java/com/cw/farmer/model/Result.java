package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("centreId")
    @Expose
    private List<CentreId> centreId = null;
    @SerializedName("base64EncodedAuthenticationKey")
    @Expose
    private String base64EncodedAuthenticationKey;
    @SerializedName("authenticated")
    @Expose
    private boolean authenticated;
    @SerializedName("roles")
    @Expose
    private List<Role> roles = null;
    @SerializedName("permissions")
    @Expose
    private List<String> permissions = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shouldRenewPassword")
    @Expose
    private boolean shouldRenewPassword;
    @SerializedName("isTwoFactorAuthenticationRequired")
    @Expose
    private boolean isTwoFactorAuthenticationRequired;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Result withUsername(String username) {
        this.username = username;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Result withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public List<CentreId> getCentreId() {
        return centreId;
    }

    public void setCentreId(List<CentreId> centreId) {
        this.centreId = centreId;
    }

    public Result withCentreId(List<CentreId> centreId) {
        this.centreId = centreId;
        return this;
    }

    public String getBase64EncodedAuthenticationKey() {
        return base64EncodedAuthenticationKey;
    }

    public void setBase64EncodedAuthenticationKey(String base64EncodedAuthenticationKey) {
        this.base64EncodedAuthenticationKey = base64EncodedAuthenticationKey;
    }

    public Result withBase64EncodedAuthenticationKey(String base64EncodedAuthenticationKey) {
        this.base64EncodedAuthenticationKey = base64EncodedAuthenticationKey;
        return this;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Result withAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Result withRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Result withPermissions(List<String> permissions) {
        this.permissions = permissions;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Result withName(String name) {
        this.name = name;
        return this;
    }

    public boolean isShouldRenewPassword() {
        return shouldRenewPassword;
    }

    public void setShouldRenewPassword(boolean shouldRenewPassword) {
        this.shouldRenewPassword = shouldRenewPassword;
    }

    public Result withShouldRenewPassword(boolean shouldRenewPassword) {
        this.shouldRenewPassword = shouldRenewPassword;
        return this;
    }

    public boolean isIsTwoFactorAuthenticationRequired() {
        return isTwoFactorAuthenticationRequired;
    }

    public void setIsTwoFactorAuthenticationRequired(boolean isTwoFactorAuthenticationRequired) {
        this.isTwoFactorAuthenticationRequired = isTwoFactorAuthenticationRequired;
    }

    public Result withIsTwoFactorAuthenticationRequired(boolean isTwoFactorAuthenticationRequired) {
        this.isTwoFactorAuthenticationRequired = isTwoFactorAuthenticationRequired;
        return this;
    }

}