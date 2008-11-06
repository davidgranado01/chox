package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalVehicleExtra {

    private long ID = -1;
    private long rentalVehicleID = -1;
    private long extraID = -1;

    
    public static ArrayList<RentalVehicleExtra> getForRental(DBConnectionWrapper connection, RentalVehicle rentalVehicle) throws SQLException {
        ArrayList<RentalVehicleExtra> l = new ArrayList<RentalVehicleExtra>();
        PreparedStatement s = connection.prepareStatement("select id from rental_vehicle_extra where rental_vehicle_id=?");
        s.setLong(1, rentalVehicle.getID());
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }
    
    public static void removeForRentalVehicle(DBConnectionWrapper connection, RentalVehicle rentalVehicle) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from rental_vehicle_extra where rental_vehicle_id=?");
        s.setLong(1, rentalVehicle.getID());
        s.executeUpdate();
        s.close();
    }

    public static RentalVehicleExtra instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalVehicleExtra.instantiate";
        try {
            return (RentalVehicleExtra) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_vehicle_id,extra_id from rental_vehicle_extra where id=?");
            s.setLong(1, id);
            RentalVehicleExtra o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalVehicleExtra();
                o.setRentalVehicleID(rs.getLong(1));
                o.setExtraID(rs.getLong(2));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_vehicle_extra(rental_vehicle_id,extra_id) values (?,?) returning id");
            s.setLong(1, getRentalVehicleID());
            s.setLong(2, getExtraID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_vehicle_extra set rental_vehicle_id=?,extra_id=? where id=?");
            s.setLong(1, getRentalVehicleID());
            s.setLong(2, getExtraID());
            s.setLong(3, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getRentalVehicleID() {
        return rentalVehicleID;
    }

    public void setRentalVehicleID(long rentalVehicleID) {
        this.rentalVehicleID = rentalVehicleID;
    }

    public long getExtraID() {
        return extraID;
    }

    public void setExtraID(long extraID) {
        this.extraID = extraID;
    }
}
