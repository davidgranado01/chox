package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleClass {

    private long ID = -1;
    private String code;

    public static ArrayList<VehicleClass> getAll(DBConnectionWrapper connection) throws SQLException
    {
        ArrayList<VehicleClass> l=new ArrayList<VehicleClass>();
        
        PreparedStatement s=connection.prepareStatement("select id from vehicle_class order by code");
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();
        
        return l;
    }
    
    public static VehicleClass getByName(DBConnectionWrapper connection, String name) throws SQLException
    {
        VehicleClass c=null;
        
        PreparedStatement s=connection.prepareStatement("select id from vehicle_class where code=?");
        s.setString(1,name);
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();
        
        return c;
    }
    
    public static VehicleClass instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.VehicleClass.instantiate";
        try {
            return (VehicleClass) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select code from vehicle_class where id=?");
            s.setLong(1, id);
            VehicleClass o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new VehicleClass();
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
            PreparedStatement s = connection.prepareStatement("insert into vehicle_class(code) values (?) returning id");
            s.setString(1, getCode());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update vehicle_class set code=? where id=?");
            s.setString(1, getCode());
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
