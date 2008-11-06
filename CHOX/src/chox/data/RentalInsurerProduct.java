package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class RentalInsurerProduct {

    private long ID = -1;
    private long rentalID=-1;
    private long insurerCountryProductID=-1;
    
    public static RentalInsurerProduct getForRental(DBConnectionWrapper connection,Rental r) throws SQLException {
        RentalInsurerProduct ri=null;
        PreparedStatement s=connection.prepareStatement("select id from rental_insurer_product where rental_id=?");
        s.setLong(1,r.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next())
            ri=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return ri;
    }
    
    public static RentalInsurerProduct instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalInsurerProduct.instantiate";
        try {
            return (RentalInsurerProduct) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,insurer_country_product_id from rental_insurer_product where id=?");
            s.setLong(1, id);
            RentalInsurerProduct o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalInsurerProduct();
                o.setRentalID(rs.getLong(1));
                o.setInsurerCountryProductID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_insurer_product(rental_id,insurer_country_product_id) values (?,?) returning id");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryProductID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_insurer_product set rental_id=?,insurer_country_product_id=? where id=?");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryProductID());
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

    public long getInsurerCountryProductID() {
        return insurerCountryProductID;
    }

    public void setInsurerCountryProductID(long insurerCountryProductID) {
        this.insurerCountryProductID = insurerCountryProductID;
    }

    
    
    
}
