package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Invoice {

    private long ID = -1;
    private long rentalID = -1;
    private BigDecimal net;
    private BigDecimal VAT;
    private BigDecimal gross;

    public static Invoice getForRental(DBConnectionWrapper connection,Rental rental) throws SQLException {
        Invoice c=null;        
        PreparedStatement s=connection.prepareStatement("select id from invoice where rental_id=?");
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
        PreparedStatement s=connection.prepareStatement("delete from invoice where rental_id=?");
        s.setLong(1,rental.getID());
        s.executeUpdate();
        s.close();        
    }
    
    public static Invoice instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Invoice.instantiate";
        try {
            return (Invoice) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,net,vat,gross from invoice where id=?");
            s.setLong(1, id);
            Invoice o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Invoice();
                o.setRentalID(rs.getLong(1));
                o.setNet(rs.getBigDecimal(2));
                o.setVAT(rs.getBigDecimal(3));
                o.setGross(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into invoice(rental_id,net,vat,gross) values (?,?,?,?) returning id");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNet());
            s.setBigDecimal(3, getVAT());
            s.setBigDecimal(4, getGross());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update invoice set rental_id=?,net=?,vat=?,gross=? where id=?");
            s.setLong(1, getRentalID());
            s.setBigDecimal(2, getNet());
            s.setBigDecimal(3, getVAT());
            s.setBigDecimal(4, getGross());
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

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getVAT() {
        return VAT;
    }

    public void setVAT(BigDecimal VAT) {
        this.VAT = VAT;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }


}
