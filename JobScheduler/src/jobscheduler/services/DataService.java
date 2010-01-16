package jobscheduler.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class DataService {

    String databaseServer = "";
    String databaseConnection = "";
    String databaseUserName = "";
    String databaseUserPsw = "";

    public Connection getConnection() throws Exception {

        Connection c = null;

        try {
        
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream("database.xml");
            prop.loadFromXML(fis);

            this.databaseServer = prop.getProperty("databaseServer");
            this.databaseConnection = prop.getProperty("databaseConnection");
            this.databaseUserName = prop.getProperty("databaseUserName");
            this.databaseUserPsw = prop.getProperty("databaseUserPsw");

            if (isDriverExist()) {
                c = DriverManager.getConnection("jdbc:postgresql://" + databaseServer + "/" + databaseConnection, databaseUserName, databaseUserPsw);
            }

        } catch (SQLException se) {
            System.out.println("Couldn't connect: print out a stack trace and exit.");
            se.printStackTrace();
            System.exit(1);
        }

        return c;
    }

    private boolean isDriverExist() {

        boolean bFlag = true;

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException cnfe) {
            bFlag = false;
            cnfe.printStackTrace();
            System.exit(1);
        }

        return bFlag;
    }
}
