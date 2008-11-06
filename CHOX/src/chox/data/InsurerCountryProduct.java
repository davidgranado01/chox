package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class InsurerCountryProduct {

    private long ID = -1;
    private long insurerCountryID = -1;
    private String name;
    private String catchAll="n";

    
    public static InsurerCountryProduct getCatchAllForRental(DBConnectionWrapper connection,Rental rental) throws SQLException
    {
        Claim c=Claim.getForRental(connection, rental);
        PreparedStatement s=connection.prepareStatement("select id from insurer_country_product where insurer_country_id=? and catch_all='y'");
        s.setLong(1,c.getTpInsurerCountryID());
        ResultSet rs=s.executeQuery();
        InsurerCountryProduct l=null;
        if(rs.next()) {
            l=InsurerCountryProduct.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        return l;
    }
        
    
    public static InsurerCountryProduct instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.InsurerCountryProduct.instantiate";
        try {
            return (InsurerCountryProduct) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select insurer_country_id,name,catch_all from insurer_country_product where id=?");
            s.setLong(1, id);
            InsurerCountryProduct o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new InsurerCountryProduct();
                o.setInsurerCountryID(rs.getLong(1));
                o.setName(rs.getString(2));
                o.setCatchAll(rs.getString(3));
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
            PreparedStatement s = connection.prepareStatement("insert into insurer_country_product(insurer_country_id,name,catch_all) values (?,?,?) returning id");
            s.setLong(1,getInsurerCountryID());
            s.setString(2,getName());
            s.setString(3,getCatchAll());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update insurer_country_product set insurer_country_id=?,name=?,catch_all=? where id=?");
            s.setLong(1,getInsurerCountryID());
            s.setString(2,getName());
            s.setString(3,getCatchAll());
            s.setLong(4, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getInsurerCountryID() {
        return insurerCountryID;
    }

    public void setInsurerCountryID(long insurerCountryID) {
        this.insurerCountryID = insurerCountryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchAll() {
        return catchAll;
    }

    public void setCatchAll(String catchAll) {
        this.catchAll = catchAll;
    }

    
}
