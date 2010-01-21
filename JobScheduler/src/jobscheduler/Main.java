package jobscheduler;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jobscheduler.services.DataService;

public class Main {

    public static void main(String[] args) throws Exception {

        PreparedStatement ps = null;

        DataService dataService = new DataService();

        try{

            Connection conn = dataService.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select sqlrunstatusreport("+999+")");
            conn.close();

        } catch (SQLException ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

}
