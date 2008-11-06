package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class RentalNoteAcknowledgement {

    private long ID = -1;
    private long rentalID = -1;
    private long sessionID=-1;

    public static RentalNoteAcknowledgement instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalNoteAcknowledgement.instantiate";
        try {
            return (RentalNoteAcknowledgement) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select note_id,session_id from rental_note_acknowledgement where id=?");
            s.setLong(1, id);
            RentalNoteAcknowledgement o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalNoteAcknowledgement();
                o.setRentalID(rs.getLong(1));
                o.setSessionID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_note_acknowledgement(rental_id,session_id) values (?,?) returning id");
            s.setLong(1,getRentalID());
            s.setLong(2,getSessionID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_note_acknowledgement set rental_id=?,session_id=? where id=?");
            s.setLong(1,getRentalID());
            s.setLong(2,getSessionID());
            s.setLong(3, getID());
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

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    
}
