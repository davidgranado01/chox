package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalExtraRate {

    private long ID = -1;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private long extraID=-1;
    private BigDecimal cost;
    
    public static RentalExtraRate getRentalExtraRate(DBConnectionWrapper connection,InsurerCountry insurerCountry,Supplier supplier,Extra extra) throws SQLException
    {
        PreparedStatement s=connection.prepareStatement("select id from rental_extra_rate where insurer_country_id=? and supplier_id=? and extra_id=?");
        s.setLong(1,insurerCountry.getID());
        s.setLong(2,supplier.getID());
        s.setLong(3,extra.getID());
        ResultSet rs=s.executeQuery();
        RentalExtraRate r=null;
        if(rs.next())
            r=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return r;
    }

    public static RentalExtraRate instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalExtraRate.instantiate";
        try {
            return (RentalExtraRate) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select insurer_country_id,supplier_id,extra_id,cost from rental_extra_rate where id=?");
            s.setLong(1, id);
            RentalExtraRate o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalExtraRate();
                o.setInsurerCountryID(rs.getLong(1));
                o.setSupplierID(rs.getLong(2));
                o.setExtraID(rs.getLong(3));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_extra_rate(insurer_country_id,supplier_id,extra_id,cost) values (?,?,?,?) returning id");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setLong(3,getExtraID());
            s.setBigDecimal(4,getCost());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_extra_rate set insurer_country_id=?,supplier_id=?,extra_id=?,cost=? where id=?");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setLong(3,getExtraID());
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

    public long getExtraID() {
        return extraID;
    }

    public void setExtraID(long extraID) {
        this.extraID = extraID;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

  

   

    

}
