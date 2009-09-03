/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jobscheduler.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class DataService {

    
    final String databaseServer = "localhost:5432";
    final String databaseConnection = "choxidas_prod";
    final String databaseUserName = "postgres";
    final String databaseUserPsw= "GreenF1nCHPasswurd99.";
    
    /*
    final String databaseServer = "10.0.0.2:5432";
    final String databaseConnection = "chox_test";
    final String databaseUserName = "postgres";
    final String databaseUserPsw= "GreenF1nCH";
    */
    
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
