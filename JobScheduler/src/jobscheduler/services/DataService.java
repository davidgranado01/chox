package jobscheduler.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class DataService {

    public Connection getConnection() throws Exception {

        Connection c = null;

        try {

            System.out.println(">>>>>>>>>>>>"+new File(".").getAbsolutePath());

            File configFile = new File("database.xml");
            FileInputStream fis = new FileInputStream(configFile);

            // InputStream fis = new ClassPathResource("database.xls").getInputStream();
            Properties prop = new Properties();
            prop.loadFromXML(fis);

            String  databaseServer = prop.getProperty("databaseServer");
            String  databaseConnection = prop.getProperty("databaseConnection");
            String  databaseUserName = prop.getProperty("databaseUserName");
            String  databaseUserPsw = prop.getProperty("databaseUserPsw");

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
