package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class RentalNote {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID = -1;
    private String note;
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    
    private String filename=null;
    private String fileMIMEType=null;
    private byte [] fileBLOB=null;

    public static RentalNote getByUUID(DBConnectionWrapper connection, String uuid) throws SQLException {
        RentalNote c = null;

        PreparedStatement s = connection.prepareStatement("select id from rental_note where uuid=?");
        s.setString(1, uuid);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }
    
    public static ArrayList<RentalNote> getForRental(DBConnectionWrapper connection,Rental rental) throws SQLException {
        ArrayList<RentalNote> l=new ArrayList<RentalNote>();        
        PreparedStatement s=connection.prepareStatement("select id from rental_note where rental_id=? order by created");
        s.setLong(1,rental.getID());
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();        
        return l;
    }    
        
    public static RentalNote instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalNote.instantiate";
        try {
            return (RentalNote) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,rental_id,note,created,session_id,file_name,file_mimetype,file_blob from rental_note where id=?");
            s.setLong(1, id);
            RentalNote o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalNote();
                o.setUuid(rs.getString(1));
                o.setRentalID(rs.getLong(2));
                o.setNote(rs.getString(3));
                o.setCreated(rs.getTimestamp(4));
                o.setSessionID(rs.getLong(5));
                if (rs.wasNull()) {
                    o.setSessionID(-1);
                }
                if(rs.wasNull())
                    o.setFilename(null);
                else
                    o.setFilename(rs.getString(6));
                if(rs.wasNull())
                    o.setFileMIMEType(null);
                else
                    o.setFileMIMEType(rs.getString(7));
                if(rs.wasNull())
                    o.setFileBLOB(null);
                else
                    o.setFileBLOB(rs.getBytes(8));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_note(uuid,rental_id,note,created,session_id,file_name,file_mimetype,file_blob) values (?,?,?,?,?,?,?,?) returning id");
            s.setString(1, getUuid());
            s.setLong(2, getRentalID());
            s.setString(3, getNote());
            s.setTimestamp(4, getCreated());
            if (getSessionID() == -1) {
                s.setNull(5, Types.INTEGER);
            } else {
                s.setLong(5, getSessionID());
            }
            if(getFilename()==null)
                s.setNull(6,Types.VARCHAR);
            else
                s.setString(6,getFilename());
            if(getFileMIMEType()==null)
                s.setNull(7,Types.VARCHAR);
            else
                s.setString(7,getFileMIMEType());
            if(getFileBLOB()==null)
                s.setNull(8,Types.VARBINARY);
            else
                s.setBytes(8,getFileBLOB());
            
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_note set uuid=?,rental_id=?,note=?,created=?,session_id=?,file_name=?,file_mimetype=?,file_blob=? where id=?");
            s.setString(1, getUuid());
            s.setLong(2, getRentalID());
            s.setString(3, getNote());
            s.setTimestamp(4, getCreated());
            if (getSessionID() == -1) {
                s.setNull(5, Types.INTEGER);
            } else {
                s.setLong(5, getSessionID());
            }
            if(getFilename()==null)
                s.setNull(6,Types.VARCHAR);
            else
                s.setString(6,getFilename());
            if(getFileMIMEType()==null)
                s.setNull(7,Types.VARCHAR);
            else
                s.setString(7,getFileMIMEType());
            if(getFileBLOB()==null)
                s.setNull(8,Types.VARBINARY);
            else
                s.setBytes(8,getFileBLOB());
            s.setLong(9, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileMIMEType() {
        return fileMIMEType;
    }

    public void setFileMIMEType(String fileMIMEType) {
        this.fileMIMEType = fileMIMEType;
    }

    public byte[] getFileBLOB() {
        return fileBLOB;
    }

    public void setFileBLOB(byte[] fileBLOB) {
        this.fileBLOB = fileBLOB;
    }
}
