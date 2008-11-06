package chox.daemon;

import chox.data.*;
import chox.decision.Decision;
import chox.util.FeeGenerator;
import chox.util.PaymentRequestGenerator;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import idas.alert.Alert;
import idas.configuration.DatabaseConnectionFactory;
import idas.web.security.iDASSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class AutoAuthoriser {

    private String configFile;
    public static final int MAX_RUNTIME_SECONDS = 240;
    public static final int MAX_PER_BATCH = 5000;
    public static final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        //args = new String[]{"/Users/stu/Projects/CHOX/config.xml"};

        if (args.length != 1) {
            System.err.println("Usage: chox.daemon.AutoAuthoriser <config.xml>");
            System.exit(1);
        }

        try {
            AutoAuthoriser a = new AutoAuthoriser(args[0]);
            a.process();

        } catch (Exception e) {
            Alert.message("chox.daemon.AutoAuthoriser Exception: " + e.getMessage());
            e.printStackTrace(Logger.err);
        }
    }

    private AutoAuthoriser(String configFile) {
        this.configFile = configFile;
        Alert.init(configFile);
    }

    private void process() throws Exception {
        DBConnectionWrapper connection = DatabaseConnectionFactory.newConnection(configFile);

        HashMap<Rental, DecisionMaking> rentalsToAuthorise = new HashMap<Rental, DecisionMaking>();

        PreparedStatement s = connection.prepareStatement("select rental_id,decision_id from can_auto_authorise limit " + MAX_PER_BATCH);

        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            rentalsToAuthorise.put(Rental.instantiate(connection, rs.getLong(1)), DecisionMaking.instantiate(connection, rs.getLong(2)));
        }
        rs.close();
        s.close();

        for (Rental r : rentalsToAuthorise.keySet()) {
            DecisionMaking dm = rentalsToAuthorise.get(r);
            if (System.currentTimeMillis() > startTime + (1000 * MAX_RUNTIME_SECONDS)) {
                break;
            }
            try {
                process(connection, r, dm);
            } catch (Exception e) {
                Alert.message("Error handling rentalID=" + r.getID());
                Alert.message("chox.daemon.AutoAuthoriser Exception: " + e.getMessage());
                e.printStackTrace(Logger.err);
                connection.rollback();
            }
            connection.commit();
        }

        connection.commit();
        connection.close();
    }

    private void process(DBConnectionWrapper connection, Rental r, DecisionMaking dm) throws Exception {

        Decision d = dm.getDecisionForRental(connection, r, "invoice pay");
        Boolean canAuth = d.decide(connection, r);
        if (canAuth == null || canAuth.equals(Boolean.FALSE)) {
            return;
        }
        
        RentalAuthorisation ra = new RentalAuthorisation();
        ra.setRentalID(r.getID());
        ra.setSessionID(999);
        ra.setStatus(RentalAuthorisation.INVOICE_AUTHORISED);
        ra.save(connection);

        PaymentRequestGenerator.createRentalInvoice(connection, r);
        FeeGenerator.chargeFee(connection, iDASSession.instantiate(connection, 999), r, CHOXUsageFee.AUTHORISE_INVOICE);

        r.updateInsurerLocationProduct(connection);
        r.updateSearchData(connection);

    }
}
