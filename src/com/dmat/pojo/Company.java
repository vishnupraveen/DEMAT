package com.dmat.pojo;

public class Company {

    private String companyName;
    private int companyId;
    private double sharePrice;
    private int units;

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public double getValue() {
        return sharePrice;
    }

    public void setValue(double sharePrice) {
        this.sharePrice = sharePrice;
    }
}
