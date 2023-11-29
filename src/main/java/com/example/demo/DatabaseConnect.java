package com.example.demo;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnect {
    public static void main(String[] args) {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setServerName("DESKTOP-DS7SMCV");
        ds.setPortNumber(1433);
        ds.setDatabaseName("ProjectCNPM");
        ds.setEncrypt(false);

        try {
            Connection connection = ds.getConnection();
            System.out.println(connection.getCatalog());
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
