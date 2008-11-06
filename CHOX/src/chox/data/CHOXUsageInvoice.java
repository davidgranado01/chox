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

public class CHOXUsageInvoice {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;
    private long ackSessionID=-1;
    private Timestamp ackDate=null;
    private Timestamp updated=new Timestamp(System.currentTimeMillis());
    private byte[] pdf=null;

    public static CHOXUsageInvoice instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.CHOXUsageInvoice.instantiate";
        try {
            return (CHOXUsageInvoice) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount,ack_session_id,ack_site,updated,pdf from chox_usage_invoice where id=?");
            s.setLong(1, id);
            CHOXUsageInvoice o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new CHOXUsageInvoice();
                o.setUuid(rs.getString(1));
                o.setInsurerCountryID(rs.getLong(2));
                if(rs.wasNull())
                    o.setInsurerCountryID(-1);
                o.setSupplierID(rs.getLong(3));
                if(rs.wasNull())
                    o.setSupplierID(-1);
                o.setNetAmount(rs.getBigDecimal(4));
                o.setVatAmount(rs.getBigDecimal(5));
                o.setGrossAmount(rs.getBigDecimal(6));
                o.setAckSessionID(rs.getLong(7));
                if(rs.wasNull())
                    o.setAckSessionID(-1);
                o.setAckDate(rs.getTimestamp(8));
                if(rs.wasNull())
                    o.setAckDate(null);
                o.setUpdated(rs.getTimestamp(9));
                o.setPdf(rs.getBytes(10));
                if(rs.wasNull())
                    o.setPdf(null);
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
            PreparedStatement s = connection.prepareStatement("insert into chox_usage_invoice(uuid,insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount,ack_session_id,ack_date,updated,pdf) values (?,?,?,?,?,?,?,?,?,?) returning id");
            s.setString(1,getUuid());
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
            if(getAckSessionID()==-1)
                s.setNull(7,Types.INTEGER);
            else
                s.setLong(7,getAckSessionID());
            if(getAckDate()==null)
                s.setNull(8,Types.TIMESTAMP);
            else
                s.setTimestamp(8,getAckDate());
            s.setTimestamp(9,getUpdated());
            if(getPdf()==null)
                s.setNull(10,Types.BLOB);
            else
                s.setBytes(10,getPdf());            
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update chox_usage_invoice set uuid=?,insurer_country_id=?,supplier_id=?,net_amount=?,vat_amount=?,gross_amount=?,ack_session_id=?,ack_date=?,updated=?,pdf=? where id=?");
            s.setString(1,getUuid());
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
            if(getAckSessionID()==-1)
                s.setNull(7,Types.INTEGER);
            else
                s.setLong(7,getAckSessionID());
            if(getAckDate()==null)
                s.setNull(8,Types.TIMESTAMP);
            else
                s.setTimestamp(8,getAckDate());
            s.setTimestamp(9,getUpdated());
            if(getPdf()==null)
                s.setNull(10,Types.BLOB);
            else
                s.setBytes(10,getPdf());            
            s.setLong(11, getID());
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

    public long getAckSessionID() {
        return ackSessionID;
    }

    public void setAckSessionID(long ackSessionID) {
        this.ackSessionID = ackSessionID;
    }

    public Timestamp getAckDate() {
        return ackDate;
    }

    public void setAckDate(Timestamp ackDate) {
        this.ackDate = ackDate;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    
    
   
}
