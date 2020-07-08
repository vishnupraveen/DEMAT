package com.dmat.services.api;

import com.dmat.pojo.Company;
import com.dmat.pojo.Stock;

import java.util.Date;

public interface UserService {

    public void displayDemat();
    public boolean generateShareReport();
    public boolean generateAccountReport();
    public void UserOperations();
    
}
