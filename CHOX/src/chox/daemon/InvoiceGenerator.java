package chox.daemon;

import chox.data.*;
import chox.data.RentalAuthorisation;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import idas.alert.Alert;
import idas.configuration.DatabaseConnectionFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class InvoiceGenerator {

    private String configFile;
    public static final int MAX_RUNTIME_SECONDS = 240;
    public static final int MAX_PER_BATCH=5000;
    public static final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        //args = new String[]{"/Users/stu/Projects/CHOX/config.xml"};

        if (args.length != 1) {
            System.err.println("Usage: chox.daemon.InvoiceGenerator <config.xml>");
            System.exit(1);
        }

        try {
            InvoiceGenerator a = new InvoiceGenerator(args[0]);
            a.process();

        } catch (Exception e) {
            Alert.message("chox.daemon.InvoiceGenerator Exception: "+e.getMessage());
            e.printStackTrace(Logger.err);
        }
    }

    private InvoiceGenerator(String configFile) {
        this.configFile = configFile;
        Alert.init(configFile);
    }

    private void process() throws Exception {
        DBConnectionWrapper connection = DatabaseConnectionFactory.newConnection(configFile);

        ArrayList<Rental> rentalsToInvoice = new ArrayList<Rental>();
        PreparedStatement s = connection.prepareStatement("select r.id from rental r,latest_rental_authorisation_status a where r.id=a.rental_id and r.rental_status=? and a.status=? limit "+MAX_PER_BATCH);
        s.setString(1,Rental.COMPLETE);
        s.setString(2,RentalAuthorisation.IN_PROGRESS);
        
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            rentalsToInvoice.add(Rental.instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();

        for (Rental r:rentalsToInvoice) {

            if (System.currentTimeMillis() > startTime + (1000 * MAX_RUNTIME_SECONDS)) {
                break;
            }
            try {
                process(connection, r);
            } catch (Exception e) {
                Alert.message("Error handling rentalID=" + r.getID());
                Alert.message("chox.daemon.InvoiceGenerator Exception: "+e.getMessage());
                e.printStackTrace(Logger.err);
                connection.rollback();
            }
            connection.commit();
        }

        connection.commit();
        connection.close();
    }

    private void process(DBConnectionWrapper connection, Rental r) throws Exception {

        Message m=Message.getLatestMessage(connection, r);               
        
        RentalAuthorisation ra=new RentalAuthorisation();
        ra.setRentalID(r.getID());
        ra.setStatus(RentalAuthorisation.RENTAL_ENDED);
        ra.setSessionID(m.getSessionID());
        ra.save(connection);
    }
}
