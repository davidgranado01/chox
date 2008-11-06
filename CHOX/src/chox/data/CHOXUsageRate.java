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

public class CHOXUsageRate {

    private long ID = -1;
    private String feeType;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;
    
    public static CHOXUsageRate getRate(DBConnectionWrapper connection,String feeType,InsurerCountry insurerCountry) throws SQLException
    {
        CHOXUsageRate f=null;
        
        PreparedStatement s=connection.prepareStatement("select id from chox_usage_rate where insurer_country_id=? and fee_type=?");
        s.setLong(1,insurerCountry.getID());
        s.setString(2,feeType);
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            f=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();
        return f;
    }

    public static CHOXUsageRate getRate(DBConnectionWrapper connection,String feeType,Supplier supplier) throws SQLException
    {
        CHOXUsageRate f=null;
        
        PreparedStatement s=connection.prepareStatement("select id from chox_usage_rate where supplier_id=? and fee_type=?");
        s.setLong(1,supplier.getID());
        s.setString(2,feeType);
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            f=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();
        return f;
    }

    public static CHOXUsageRate instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.CHOXUsageRate.instantiate";
        try {
            return (CHOXUsageRate) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select fee_type,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount from chox_usage_rate where id=?");
            s.setLong(1, id);
            CHOXUsageRate o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new CHOXUsageRate();
                o.setFeeType(rs.getString(1));
                o.setInsurerCountryID(rs.getLong(2));
                if(rs.wasNull())
                    o.setInsurerCountryID(-1);
                o.setSupplierID(rs.getLong(3));
                if(rs.wasNull())
                    o.setSupplierID(-1);
                o.setNetAmount(rs.getBigDecimal(4));
                o.setVatAmount(rs.getBigDecimal(5));
                o.setGrossAmount(rs.getBigDecimal(6));
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
            PreparedStatement s = connection.prepareStatement("insert into chox_usage_rate(fee_type,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount) values (?,?,?,?,?,?) returning id");
            s.setString(1,feeType);
            if(getInsurerCountryID()==-1)
                s.setNull(2,Types.INTEGER);
            else
                s.setLong(2,getInsurerCountryID());
            if(getSupplierID()==-1)
                s.setNull(3,Types.INTEGER);
            else
                s.setLong(3,getSupplierID());
            s.setBigDecimal(4,getNetAmount());
            s.setBigDecimal(5,getVatAmount());
            s.setBigDecimal(6,getGrossAmount());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update chox_usage_rate set fee_type=?,insurer_country_id=?,supplier_id=?,net_amount=?,vat_amount=?,gross_amount=? where id=?");
            s.setString(1,feeType);
            if(getInsurerCountryID()==-1)
                s.setNull(2,Types.INTEGER);
            else
                s.setLong(2,getInsurerCountryID());
            if(getSupplierID()==-1)
                s.setNull(3,Types.INTEGER);
            else
                s.setLong(3,getSupplierID());
            s.setBigDecimal(4,getNetAmount());
            s.setBigDecimal(5,getVatAmount());
            s.setBigDecimal(6,getGrossAmount());
            s.setLong(7, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
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
