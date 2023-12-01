package com.example.cnpm;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {

    public static Connection connectDb(){

        try{

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "");
            return connect;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to connect to the database!");
        }
        return null;
    }

}
