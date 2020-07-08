package com.dmat.datasource.api;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public interface Transaction  {

    public ResultSet read(String query);
    public int update(String query);
    public int update(ArrayList<String> query );
}
