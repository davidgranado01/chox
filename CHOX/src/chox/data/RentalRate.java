package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalRate {

    private long ID = -1;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private long vehicleClassID=-1;
    private BigDecimal cost;
    
    public static RentalRate getRentalRate(DBConnectionWrapper connection,InsurerCountry insurerCountry,Supplier supplier,VehicleClass vehicleClass) throws SQLException
    {
        PreparedStatement s=connection.prepareStatement("select id from rental_rate where insurer_country_id=? and supplier_id=? and vehicle_class_id=?");
        s.setLong(1,insurerCountry.getID());
        s.setLong(2,supplier.getID());
        s.setLong(3,vehicleClass.getID());
        ResultSet rs=s.executeQuery();
        RentalRate rr=null;
        if(rs.next())
            rr=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return rr;        
    }

    public static RentalRate instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalRate.instantiate";
        try {
            return (RentalRate) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select insurer_country_id,supplier_id,vehicle_class_id,cost from rental_rate where id=?");
            s.setLong(1, id);
            RentalRate o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalRate();
                o.setInsurerCountryID(rs.getLong(1));
                o.setSupplierID(rs.getLong(2));
                o.setVehicleClassID(rs.getLong(3));
                o.setCost(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) values (?,?,?,?) returning id");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setLong(3,getVehicleClassID());
            s.setBigDecimal(4,getCost());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_rate set insurer_country_id=?,supplier_id=?,vehicle_class_id=?,cost=? where id=?");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setLong(3,getVehicleClassID());
            s.setBigDecimal(4,getCost());
            s.setLong(5, getID());
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

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public long getVehicleClassID() {
        return vehicleClassID;
    }

    public void setVehicleClassID(long vehicleClassID) {
        this.vehicleClassID = vehicleClassID;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

   

    

}
