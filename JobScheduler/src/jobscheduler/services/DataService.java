/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jobscheduler.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class DataService {

    final String databaseConnection = "chox";
    final String databaseUserName = "chox";
    final String databaseUserPsw= "chox";
    
    public Connection getConnection(){

        Connection c = null;
        
        try {
            
            if(isDriverExist()){
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseConnection, databaseUserName, databaseUserPsw);
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
