package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalExtra {

    private long ID = -1;
    private long rentalID = -1;
    private long extraID = -1;
    private BigDecimal quantity;
    private BigDecimal itemAmount;

    public static ArrayList<RentalExtra> getForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        ArrayList<RentalExtra> l = new ArrayList<RentalExtra>();
        PreparedStatement s = connection.prepareStatement("select id from rental_extra where rental_id=?");
        s.setLong(1, rental.getID());
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }

    public static void removeForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from rental_extra where rental_id=?");
        s.setLong(1, rental.getID());
        s.executeUpdate();
        s.close();
    }

    public static RentalExtra instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalExtra.instantiate";
        try {
            return (RentalExtra) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,extra_id,quantity,item_amount from rental_extra where id=?");
            s.setLong(1, id);
            RentalExtra o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalExtra();
                o.setRentalID(rs.getLong(1));
                o.setExtraID(rs.getLong(2));
                o.setQuantity(rs.getBigDecimal(3));
                o.setItemAmount(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_extra(rental_id,extra_id,quantity,item_amount) values (?,?,?,?) returning id");
            s.setLong(1, getRentalID());
            s.setLong(2, getExtraID());
            s.setBigDecimal(3, getQuantity());
            s.setBigDecimal(4, getItemAmount());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_extra set rental_id=?,extra_id=?,quantity=?,item_amount=? where id=?");
            s.setLong(1, getRentalID());
            s.setLong(2, getExtraID());
            s.setBigDecimal(3, getQuantity());
            s.setBigDecimal(4, getItemAmount());
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

    public long getExtraID() {
        return extraID;
    }

    public void setExtraID(long extraID) {
        this.extraID = extraID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }
}
