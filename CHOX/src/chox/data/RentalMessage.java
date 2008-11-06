package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalMessage {

    private long ID = -1;
    private long rentalID=-1;
    private long messageID=-1;

    public static RentalMessage instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalMessage.instantiate";
        try {
            return (RentalMessage) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,message_id from rental_message where id=?");
            s.setLong(1, id);
            RentalMessage o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalMessage();
                o.setRentalID(rs.getLong(1));
                o.setMessageID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_message(rental_id,message_id) values (?,?) returning id");
            s.setLong(1,getRentalID());
            s.setLong(2,getMessageID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_message set rental_id=?,message_id=? where id=?");
            s.setLong(1,getRentalID());
            s.setLong(2,getMessageID());
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

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    

}
