package com.dmat.services.api;

import com.dmat.pojo.Company;

import java.util.Date;
import java.util.List;

public interface AdminService {

    public void addCompany();
    public void restrictCompany();
    public void generateStockTrend();
    public void generateEarningReport();
    public boolean displayStock(String condition);
    public void activateCompany();
    public void adminOperations();
    public void updateStockInfo();
}
