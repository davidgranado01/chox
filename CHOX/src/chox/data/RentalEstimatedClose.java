package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class RentalEstimatedClose {

    private long ID = -1;
    private Timestamp estimatedClose;
    private long rentalNoteID = -1;

    public static RentalEstimatedClose getForRentalNote(DBConnectionWrapper connection,RentalNote rentalNote) throws SQLException {
        RentalEstimatedClose c=null;
        PreparedStatement s=connection.prepareStatement("select id from rental_estimated_close where rental_note_id=?");
        s.setLong(1,rentalNote.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next())
            c=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return c;
    }
    
    public static ArrayList<RentalEstimatedClose> getForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        ArrayList<RentalEstimatedClose> l = new ArrayList<RentalEstimatedClose>();
        PreparedStatement s = connection.prepareStatement("select ec.id from rental_estimated_close ec,rental_note rn where rn.id=ec.rental_note_id and rn.rental_id=? order by rn.created");
        s.setLong(1, rental.getID());
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }

    public static RentalEstimatedClose instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalEstimatedClose.instantiate";
        try {
            return (RentalEstimatedClose) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select estimated_close,rental_note_id from rental_estimated_close where id=?");
            s.setLong(1, id);
            RentalEstimatedClose o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalEstimatedClose();
                o.setEstimatedClose(rs.getTimestamp(1));
                o.setRentalNoteID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_estimated_close(estimated_close,rental_note_id) values (?,?) returning id");
            s.setTimestamp(1, getEstimatedClose());
            s.setLong(2,getRentalNoteID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_estimated_close set estimated_close=?,rental_note_id=? where id=?");
            s.setTimestamp(1, getEstimatedClose());
            s.setLong(2,getRentalNoteID());
            s.setLong(3, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public Timestamp getEstimatedClose() {
        return estimatedClose;
    }

    public void setEstimatedClose(Timestamp estimatedClose) {
        this.estimatedClose = estimatedClose;
    }

    public long getRentalNoteID() {
        return rentalNoteID;
    }

    public void setRentalNoteID(long rentalNoteID) {
        this.rentalNoteID = rentalNoteID;
    }

    
    
}
