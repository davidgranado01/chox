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

public class CHOXUsageFee {
    
    public static final String UPLOAD_RENTAL="upload rental";
    public static final String REQUEST_AUTHORISATION="request authorisation";
    public static final String SELF_AUTHORISATION="self authorisation";
    public static final String AUTHORISE_RENTAL="authorise rental";
    public static final String ACKNOWLEDGE_AUTHORISATION="acknowledge authorisation";
    public static final String DISPUTE_RENTAL="dispute rental";
    public static final String INVOICE="invoice";
    public static final String DISPUTE_INVOICE="dispute invoice";
    public static final String AUTHORISE_INVOICE="authorise invoice";
    public static final String CANCEL="cancel";
    
    private long ID = -1;
    private String feeType;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;
    private long rentalID=-1;
    private long sessionID=-1;
    private Timestamp updated=new Timestamp(System.currentTimeMillis());
    private long invoiceID=-1;

    public static CHOXUsageFee instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.CHOXUsageFee.instantiate";
        try {
            return (CHOXUsageFee) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select fee_type,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount,rental_id,session_id,updated,invoice_id from chox_usage_fee where id=?");
            s.setLong(1, id);
            CHOXUsageFee o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new CHOXUsageFee();
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
                o.setRentalID(rs.getLong(7));
                o.setSessionID(rs.getLong(8));
                o.setUpdated(rs.getTimestamp(9));
                o.setInvoiceID(rs.getLong(10));
                if(rs.wasNull())
                    o.setInvoiceID(-1);
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
            PreparedStatement s = connection.prepareStatement("insert into chox_usage_fee(fee_type,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount,rental_id,session_id,updated,invoice_id) values (?,?,?,?,?,?,?,?,?,?) returning id");
            s.setString(1,getFeeType());
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
            s.setLong(7,getRentalID());
            s.setLong(8,getSessionID());
            s.setTimestamp(9,getUpdated());
            if(getInvoiceID()==-1)
                s.setNull(10,Types.INTEGER);
            else
                s.setLong(10,getInvoiceID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update chox_usage_invoice set fee_type=?,insurer_country_id=?,supplier_id=?,net_amount=?,vat_amount=?,gross_amount=?,rental_id=?,session_id,updated=?,invoice_id=? where id=?");
            s.setString(1,getFeeType());
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
            s.setLong(7,getRentalID());
            s.setLong(8,getSessionID());
            s.setTimestamp(9,getUpdated());
            if(getInvoiceID()==-1)
                s.setNull(10,Types.INTEGER);
            else
                s.setLong(10,getInvoiceID());
            s.setLong(11, getID());
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

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public long getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(long invoiceID) {
        this.invoiceID = invoiceID;
    }

    

    
    
   
}
