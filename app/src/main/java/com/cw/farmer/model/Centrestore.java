package com.cw.farmer.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Centrestore implements List<Centrestore> {

    @SerializedName("invid")
    @Expose
    private String invid;
    @SerializedName("centreid")
    @Expose
    private String centreid;
    @SerializedName("qty")
    @Expose
    private String qty;

    public String getInvid() {
        return invid;
    }

    public void setInvid(String invid) {
        this.invid = invid;
    }

    public Centrestore withInvid(String invid) {
        this.invid = invid;
        return this;
    }

    public String getCentreid() {
        return centreid;
    }

    public void setCentreid(String centreid) {
        this.centreid = centreid;
    }

    public Centrestore withCentreid(String centreid) {
        this.centreid = centreid;
        return this;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Centrestore withQty(String qty) {
        this.qty = qty;
        return this;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Centrestore> iterator() {
        return null;
    }

    @Nullable
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(@Nullable T[] ts) {
        return null;
    }

    @Override
    public boolean add(Centrestore centrestore) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Centrestore> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends Centrestore> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Centrestore get(int i) {
        return null;
    }

    @Override
    public Centrestore set(int i, Centrestore centrestore) {
        return null;
    }

    @Override
    public void add(int i, Centrestore centrestore) {

    }

    @Override
    public Centrestore remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<Centrestore> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Centrestore> listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List<Centrestore> subList(int i, int i1) {
        return null;
    }
}