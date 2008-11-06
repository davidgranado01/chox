package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Extra {

    private long ID = -1;
    private String code;

    public static Extra getByName(DBConnectionWrapper connection,String name) throws SQLException
    {
        PreparedStatement s=connection.prepareStatement("select id from extra where code=?");
        s.setString(1,name);
        ResultSet rs=s.executeQuery();
        Extra c=null;
        if(rs.next())
            c=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        
        return c;
    }
    
    public static Extra instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Extra.instantiate";
        try {
            return (Extra) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select code from extra where id=?");
            s.setLong(1, id);
            Extra o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Extra();
                o.setCode(rs.getString(1));
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
            PreparedStatement s = connection.prepareStatement("insert into extra(code) values (?) returning id");
            s.setString(1,getCode());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update extra set code=? where id=?");
            s.setString(1,getCode());
            s.setLong(2, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
}
