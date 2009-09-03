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

    /**
     * 1. CHECK "select value from global_configuration where parameter='attachment_path'"
     *    TO MAKE SURE THE ATTACHMENT FOLDER ARE CORRECT
     * 2. GO TO DataService.java TO MAKE SURE POINTING TO CORRECT DATABASE
     * 3. RUN "pre-execute-sql.txt"
     * 4. EXECUTE THE JAR FILE "java -far {FileName}"
     * 5. RUN "post-execute-sql.txt"
     **/

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
