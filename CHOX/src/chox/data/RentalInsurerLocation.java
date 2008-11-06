package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalInsurerLocation {

    private long ID = -1;
    private long rentalID=-1;
    private long insurerCountryLocationID=-1;
    
    
    public static RentalInsurerLocation getForRental(DBConnectionWrapper connection,Rental r) throws SQLException {
        RentalInsurerLocation ri=null;
        PreparedStatement s=connection.prepareStatement("select id from rental_insurer_location where rental_id=?");
        s.setLong(1,r.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next())
            ri=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return ri;
    }
    
    public static RentalInsurerLocation instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalInsurerLocation.instantiate";
        try {
            return (RentalInsurerLocation) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,insurer_country_location_id from rental_insurer_location where id=?");
            s.setLong(1, id);
            RentalInsurerLocation o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalInsurerLocation();
                o.setRentalID(rs.getLong(1));
                o.setInsurerCountryLocationID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_insurer_location(rental_id,insurer_country_location_id) values (?,?) returning id");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryLocationID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_insurer_location set rental_id=?,insurer_country_location_id=? where id=?");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryLocationID());
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

    public long getInsurerCountryLocationID() {
        return insurerCountryLocationID;
    }

    public void setInsurerCountryLocationID(long insurerCountryLocationID) {
        this.insurerCountryLocationID = insurerCountryLocationID;
    }

    

    
}
