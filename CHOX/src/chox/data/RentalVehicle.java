package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class RentalVehicle {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID = -1;
    private String vehicleRegistration;
    private String vehicleManufacturer;
    private String vehicleModel;
    private long vehicleClassID = -1;
    private Timestamp rentalStart;
    private Timestamp rentalEnd;
    private BigDecimal days;

    
    public static ArrayList<RentalVehicle> getForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        ArrayList<RentalVehicle> l = new ArrayList<RentalVehicle>();
        PreparedStatement s = connection.prepareStatement("select id from rental_vehicle where rental_id=?");
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

        PreparedStatement s = connection.prepareStatement("delete from rental_vehicle_extra where rental_vehicle_id in (select id from rental_vehicle where rental_id=?)");
        s.setLong(1, rental.getID());
        s.executeUpdate();
        s.close();

        s = connection.prepareStatement("delete from rental_vehicle where rental_id=?");
        s.setLong(1, rental.getID());
        s.executeUpdate();
        s.close();
    }

    public static RentalVehicle instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalVehicle.instantiate";
        try {
            return (RentalVehicle) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,rental_id,vehicle_registration,vehicle_manufacturer,vehicle_model,vehicle_class_id,rental_start,rental_end,days from rental_vehicle where id=?");
            s.setLong(1, id);
            RentalVehicle o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalVehicle();
                o.setUuid(rs.getString(1));
                o.setRentalID(rs.getLong(2));
                o.setVehicleRegistration(rs.getString(3));
                o.setVehicleManufacturer(rs.getString(4));
                o.setVehicleModel(rs.getString(5));
                o.setVehicleClassID(rs.getLong(6));
                o.setRentalStart(rs.getTimestamp(7));
                o.setRentalEnd(rs.getTimestamp(8));
                if (rs.wasNull()) {
                    o.setRentalEnd(null);
                }
                o.setDays(rs.getBigDecimal(9));
                if (rs.wasNull()) {
                    o.setDays(null);
                }
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
            PreparedStatement s = connection.prepareStatement("insert into rental_vehicle(uuid,rental_id,vehicle_registration,vehicle_manufacturer,vehicle_model,vehicle_class_id,rental_start,rental_end,days) values (?,?,?,?,?,?,?,?,?) returning id");
            s.setString(1, getUuid());
            s.setLong(2, getRentalID());
            s.setString(3, getVehicleRegistration());
            s.setString(4, getVehicleManufacturer());
            s.setString(5, getVehicleModel());
            s.setLong(6, getVehicleClassID());
            s.setTimestamp(7, getRentalStart());
            if (getRentalEnd() == null) {
                s.setNull(8, Types.TIMESTAMP);
            } else {
                s.setTimestamp(8, getRentalEnd());
            }
            if (getDays() == null) {
                s.setNull(9, Types.NUMERIC);
            } else {
                s.setBigDecimal(9, getDays());
            }
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_vehicle set uuid=?,rental_id=?,vehicle_registration=?,vehicle_manufacturer=?,vehicle_model=?,vehicle_class_id=?,rental_start=?,rental_end=?,days=? where id=?");
            s.setString(1, getUuid());
            s.setLong(2, getRentalID());
            s.setString(3, getVehicleRegistration());
            s.setString(4, getVehicleManufacturer());
            s.setString(5, getVehicleModel());
            s.setLong(6, getVehicleClassID());
            s.setTimestamp(7, getRentalStart());
            if (getRentalEnd() == null) {
                s.setNull(8, Types.TIMESTAMP);
            } else {
                s.setTimestamp(8, getRentalEnd());
            }
            if (getDays() == null) {
                s.setNull(9, Types.NUMERIC);
            } else {
                s.setBigDecimal(9, getDays());
            }
            s.setLong(10, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public long getVehicleClassID() {
        return vehicleClassID;
    }

    public void setVehicleClassID(long vehicleClassID) {
        this.vehicleClassID = vehicleClassID;
    }

    public Timestamp getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(Timestamp rentalStart) {
        this.rentalStart = rentalStart;
    }

    public Timestamp getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(Timestamp rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }
}
