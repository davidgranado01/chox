package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RentalCost {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal netRental;
    private BigDecimal rentalVAT;
    private BigDecimal grossRental;

    public static RentalCost getForRental(DBConnectionWrapper connection,Rental rental) throws SQLException {
        RentalCost c=null;        
        PreparedStatement s=connection.prepareStatement("select id from rental_cost where rental_id=?");
        s.setLong(1,rental.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();        
        return c;
    }    
    
    
    public static void removeForRental(DBConnectionWrapper connection,Rental rental) throws SQLException {        
        PreparedStatement s=connection.prepareStatement("delete from rental_cost where rental_id=?");
        s.setLong(1,rental.getID());
        s.executeUpdate();
        s.close();        
    }
    
    public static RentalCost instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalCost.instantiate";
        try {
            return (RentalCost) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,net_rental,rental_vat,gross_rental from rental_cost where id=?");
            s.setLong(1, id);
            RentalCost o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalCost();
                o.setRentalID(rs.getLong(1));
                o.setNetRental(rs.getBigDecimal(2));
                o.setRentalVAT(rs.getBigDecimal(3));
                o.setGrossRental(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_cost(rental_id,net_rental,rental_vat,gross_rental) values (?,?,?,?) returning id");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNetRental());
            s.setBigDecimal(3, getRentalVAT());
            s.setBigDecimal(4, getGrossRental());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_cost set rental_id=?,net_rental=?,rental_vat=?,gross_rental=? where id=?");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNetRental());
            s.setBigDecimal(3, getRentalVAT());
            s.setBigDecimal(4, getGrossRental());
            s.setLong(5, getID());
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

    public BigDecimal getNetRental() {
        return netRental;
    }

    public void setNetRental(BigDecimal netRental) {
        this.netRental = netRental;
    }

    public BigDecimal getRentalVAT() {
        return rentalVAT;
    }

    public void setRentalVAT(BigDecimal rentalVAT) {
        this.rentalVAT = rentalVAT;
    }

    public BigDecimal getGrossRental() {
        return grossRental;
    }

    public void setGrossRental(BigDecimal grossRental) {
        this.grossRental = grossRental;
    }
}
