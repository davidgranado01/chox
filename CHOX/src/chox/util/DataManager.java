package chox.util;

import chox.data.Rental;
import chox.data.RentalAuthorisation;
import chox.data.RentalPaymentRequest;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataManager {

    
    public static ArrayList<Rental> getTop100EndedRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.RENTAL_ENDED);
    }
    
    public static ArrayList<Rental> getTop100PaidRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.INVOICE_AUTHORISED);
    }
    
    public static ArrayList<Rental> getTop100AuthorisedRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.AUTHORISED);
    }
    
    public static ArrayList<Rental> getTop100InvoiceDisputedRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.INVOICE_DISPUTED);
    }
    
    public static ArrayList<Rental> getTop100InvoicedRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.INVOICED);
    }
    
    public static ArrayList<Rental> getTop100DisputedRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.DISPUTED);
    }
    
    public static ArrayList<Rental> getTop100AwaitingAuthorisationRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        return getTop100Rentals(connection,sessionID,RentalAuthorisation.AWAITING_AUTHORISATION);
    }
    
    
    public static ArrayList<Rental> getTop100Rentals(DBConnectionWrapper connection,long sessionID,String status) throws SQLException
    {
        ArrayList<Rental> l=new ArrayList<Rental>();
        PreparedStatement s=connection.prepareStatement("select distinct rental_id,updated from secure_latest_rental_authorisation_status where session_id=? and status=? order by updated limit 100");
        s.setLong(1,sessionID);
        s.setString(2,status);
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            Rental r=Rental.instantiate(connection, rs.getLong(1));
            l.add(r);
        }
        rs.close();
        s.close();
        return l;
    }
    
    public static ArrayList<Rental> getTop100NewRentals(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        ArrayList<Rental> l=new ArrayList<Rental>();
        PreparedStatement s=connection.prepareStatement("select distinct rental_id,creation_date from new_rentals_secure where session_id=? order by creation_date limit 100");
        s.setLong(1,sessionID);
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            Rental r=Rental.instantiate(connection, rs.getLong(1));
            l.add(r);
        }
        rs.close();
        s.close();
        
        return l;
    }
    
    
    public static ArrayList<RentalPaymentRequest> getTop100UnAcknowledgedPayments(DBConnectionWrapper connection,long sessionID) throws SQLException
    {
        ArrayList<RentalPaymentRequest> l=new ArrayList<RentalPaymentRequest>();
        PreparedStatement s=connection.prepareStatement("select distinct p.id from rental_payment_request p,web_session_security s where p.insurer_country_id=s.insurer_country_id and p.ack_session_id is null and s.session_id=? limit 100");
        s.setLong(1,sessionID);
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(RentalPaymentRequest.instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }
    

         
    
    
}
