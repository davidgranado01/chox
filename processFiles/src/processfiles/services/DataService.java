package processfiles.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class DataService {

    final String databaseServer = "localhost:5432";
    final String databaseConnection = "prod_mig_final";
    final String databaseUserName = "postgres";
    final String databaseUserPsw= "GreenF1nCH";

    public Connection getConnection(){

        Connection c = null;
        
        try {
            
            if(isDriverExist()){
                c = DriverManager.getConnection("jdbc:postgresql://"+databaseServer+"/" + databaseConnection, databaseUserName, databaseUserPsw);
            }
            
        } catch (SQLException se) {
            System.out.println("Couldn't connect: print out a stack trace and exit.");
            se.printStackTrace();
            System.exit(1);
        }

        return c;
    }
    
    private boolean isDriverExist(){

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