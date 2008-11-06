package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class RentalAuthorisation {

    public static final String RESET = "reset";
    public static final String AWAITING_AUTHORISATION = "awaiting authorisation";
    public static final String AUTHORISED = "authorised";
    public static final String SELF_AUTHORISED = "self authorised";
    public static final String IN_PROGRESS = "in progress";
    public static final String CANCELLED = "cancelled";
    public static final String DISPUTED = "disputed";
    public static final String RENTAL_ENDED="rental ended";
    public static final String INVOICED = "invoiced";
    public static final String INVOICE_DISPUTED = "invoice disputed";
    public static final String INVOICE_AUTHORISED = "invoice authorised";
    public static final String FINISHED="finished";
    private long ID = -1;
    private long rentalID = -1;
    private Timestamp updated = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    private String status;
    private long ackSessionID = -1;

    
    public static ArrayList<RentalAuthorisation> getAuthorisations(DBConnectionWrapper connection,Rental rental) throws SQLException {
        ArrayList<RentalAuthorisation> l=new ArrayList<RentalAuthorisation>();
        
        PreparedStatement s=connection.prepareStatement("select id from rental_authorisation where rental_id=? order by updated");
        s.setLong(1,rental.getID());
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }
    
    public static RentalAuthorisation getLatestAuthorisation(DBConnectionWrapper connection, Rental rental) throws SQLException {
        RentalAuthorisation c = null;

        PreparedStatement s = connection.prepareStatement("select auth_id from latest_rental_authorisation_status where rental_id=?");
        s.setLong(1, rental.getID());
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = RentalAuthorisation.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }

    public static RentalAuthorisation instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalAuthorisation.instantiate";
        try {
            return (RentalAuthorisation) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,updated,session_id,status,ack_session_id from rental_authorisation where id=?");
            s.setLong(1, id);
            RentalAuthorisation o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalAuthorisation();
                o.setRentalID(rs.getLong(1));
                o.setUpdated(rs.getTimestamp(2));
                o.setSessionID(rs.getLong(3));
                o.setStatus(rs.getString(4));
                o.setAckSessionID(rs.getLong(5));
                if (rs.wasNull()) {
                    o.setAckSessionID(-1);
                }
                o.ID = id;
            }
            rs.close();
            s.close();
            Cache.getInstance().put(cacheKey, id, o);
            return o;
        }
    }

    public void save(DBConnectionWrapper connection) throws SQLException {
        if (getID() == -1) {
            PreparedStatement s = connection.prepareStatement("insert into rental_authorisation(rental_id,updated,session_id,status,ack_session_id) values (?,?,?,?,?) returning id");
            s.setLong(1, getRentalID());
            s.setTimestamp(2, getUpdated());
            s.setLong(3, getSessionID());
            s.setString(4, getStatus());
            if (getAckSessionID() == -1) {
                s.setNull(5, Types.INTEGER);
            } else {
                s.setLong(5, getAckSessionID());
            }
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_authorisation set rental_id=?,updated=?,session_id=?,status=?,ack_session_id=? where id=?");
            s.setLong(1, getRentalID());
            s.setTimestamp(2, getUpdated());
            s.setLong(3, getSessionID());
            s.setString(4, getStatus());
            if (getAckSessionID() == -1) {
                s.setNull(5, Types.INTEGER);
            } else {
                s.setLong(5, getAckSessionID());
            }
            s.setLong(6, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAckSessionID() {
        return ackSessionID;
    }

    public void setAckSessionID(long ackSessionID) {
        this.ackSessionID = ackSessionID;
    }
}
