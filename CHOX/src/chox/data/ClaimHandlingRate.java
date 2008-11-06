package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class ClaimHandlingRate {

    private long ID = -1;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;

    
    public static ClaimHandlingRate getRate(DBConnectionWrapper connection,InsurerCountry insurerCountry,Supplier supplier) throws SQLException
    {
        ClaimHandlingRate r=null;
        PreparedStatement s=connection.prepareStatement("select id from claim_handling_rate where insurer_country_id=? and supplier_id=?");
        s.setLong(1,insurerCountry.getID());
        s.setLong(2,supplier.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next())
            r=instantiate(connection,rs.getLong(1));
        rs.close();
        s.close();
        return r;
    }
    
    public static ClaimHandlingRate instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.ClaimHandlingRate.instantiate";
        try {
            return (ClaimHandlingRate) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount from claim_handling_rate where id=?");
            s.setLong(1, id);
            ClaimHandlingRate o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new ClaimHandlingRate();
                o.setInsurerCountryID(rs.getLong(1));
                o.setSupplierID(rs.getLong(2));
                o.setNetAmount(rs.getBigDecimal(3));
                o.setVatAmount(rs.getBigDecimal(4));
                o.setGrossAmount(rs.getBigDecimal(5));
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
            PreparedStatement s = connection.prepareStatement("insert into claim_handling_rate(insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount) values (?,?,?,?,?) returning id");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setBigDecimal(3,getNetAmount());
            s.setBigDecimal(4,getVatAmount());
            s.setBigDecimal(5,getGrossAmount());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update claim_handling_rate set insurer_country_id=?,supplier_id=?,net_amount=?,vat_amount=?,gross_amount=? where id=?");
            s.setLong(1,getInsurerCountryID());
            s.setLong(2,getSupplierID());
            s.setBigDecimal(3,getNetAmount());
            s.setBigDecimal(4,getVatAmount());
            s.setBigDecimal(5,getGrossAmount());
            s.setLong(6, getID());
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

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    
   
}
