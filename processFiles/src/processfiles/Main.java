package processfiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import processfiles.Jobs.migrateFile;
import processfiles.services.DataService;

public class Main {

    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {

        
        DataService dataService = new DataService();
        
        try{
            
            Connection conn = dataService.getConnection();
            
            new migrateFile(conn).execute();
            
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

}
