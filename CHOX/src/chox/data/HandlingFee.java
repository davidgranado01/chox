package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HandlingFee {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal netFee;
    private BigDecimal feeVAT;
    private BigDecimal grossFee;


    public static HandlingFee getForRental(DBConnectionWrapper connection,Rental rental) throws SQLException {
        HandlingFee c=null;        
        PreparedStatement s=connection.prepareStatement("select id from handling_fee where rental_id=?");
        s.setLong(1,rental.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();        
        return c;
    }    
    
    public static void removeForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from handling_fee where rental_id=?");
        s.setLong(1, rental.getID());
        s.executeUpdate();
        s.close();
    }

    public static HandlingFee instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.HandlingFee.instantiate";
        try {
            return (HandlingFee) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,net_fee,fee_vat,gross_fee from handling_fee where id=?");
            s.setLong(1, id);
            HandlingFee o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new HandlingFee();
                o.setRentalID(rs.getLong(1));
                o.setNetFee(rs.getBigDecimal(2));
                o.setFeeVAT(rs.getBigDecimal(3));
                o.setGrossFee(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into handling_fee(rental_id,net_fee,fee_vat,gross_fee) values (?,?,?,?) returning id");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNetFee());
            s.setBigDecimal(3, getFeeVAT());
            s.setBigDecimal(4, getGrossFee());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update handling_fee set rental_id=?,net_fee=?,fee_vat=?,gross_fee=? where id=?");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNetFee());
            s.setBigDecimal(3, getFeeVAT());
            s.setBigDecimal(4, getGrossFee());
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

    public BigDecimal getNetFee() {
        return netFee;
    }

    public void setNetFee(BigDecimal netFee) {
        this.netFee = netFee;
    }

    public BigDecimal getFeeVAT() {
        return feeVAT;
    }

    public void setFeeVAT(BigDecimal feeVAT) {
        this.feeVAT = feeVAT;
    }

    public BigDecimal getGrossFee() {
        return grossFee;
    }

    public void setGrossFee(BigDecimal grossFee) {
        this.grossFee = grossFee;
    }
}
