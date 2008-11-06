package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class Message {

    private long ID = -1;
    private String message;
    private Timestamp received = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    private int statusID = 3;

    public static Message getLatestMessage(DBConnectionWrapper connection,Rental rental) throws SQLException
    {
        Message m=null;
        PreparedStatement s=connection.prepareStatement("select rm.message_id from rental_message rm,message m where m.id=rm.message_id and rm.rental_id=? order by m.received desc limit 1");
        s.setLong(1,rental.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next())
            m=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return m;
    }
    
    public static Message instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Message.instantiate";
        try {
            return (Message) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select message,received,session_id,status_id from message where id=?");
            s.setLong(1, id);
            Message o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Message();

                o.setMessage(rs.getString(1));
                o.setReceived(rs.getTimestamp(2));
                o.setSessionID(rs.getLong(3));
                if(rs.wasNull())
                    o.setSessionID(-1);
                o.setStatusID(rs.getInt(4));

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
            PreparedStatement s = connection.prepareStatement("insert into message(message,received,session_id,status_id) values (?,?,?,?) returning id");

            s.setString(1, getMessage());
            s.setTimestamp(2, getReceived());
            if (getSessionID() == -1) {
                s.setNull(3, Types.INTEGER);
            } else {
                s.setLong(3, getSessionID());
            }
            s.setInt(4, getStatusID());

            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update message set message=?,received=?,session_id=?,status_id=? where id=?");
            s.setString(1, getMessage());
            s.setTimestamp(2, getReceived());
            if (getSessionID() == -1) {
                s.setNull(3, Types.INTEGER);
            } else {
                s.setLong(3, getSessionID());
            }
            s.setInt(4, getStatusID());
            s.setLong(5, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getReceived() {
        return received;
    }

    public void setReceived(Timestamp received) {
        this.received = received;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }
}
