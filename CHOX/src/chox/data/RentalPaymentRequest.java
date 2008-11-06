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

public class RentalPaymentRequest {

    public static final String CLAIM_HANDLING="claim handling";
    public static final String RENTAL="rental";
    
    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID=-1;
    private long insurerCountryID=-1;
    private long supplierID=-1;
    private String invoiceType;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;
    private Timestamp updated = new Timestamp(System.currentTimeMillis());
    private long ackSessionID=-1;
    private Timestamp ackDate=null;

    
    public static RentalPaymentRequest getByUUID(DBConnectionWrapper connection, String uuid) throws SQLException {
        RentalPaymentRequest c = null;

        PreparedStatement s = connection.prepareStatement("select id from rental_payment_request where uuid=?");
        s.setString(1, uuid);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }    

    public static RentalPaymentRequest instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalPaymentRequest.instantiate";
        try {
            return (RentalPaymentRequest) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select rental_id,insurer_country_id,supplier_id,invoice_type,net_amount,vat_amount,gross_amount,updated,ack_session_id,ack_date,uuid from rental_payment_request where id=?");
            s.setLong(1, id);
            RentalPaymentRequest o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalPaymentRequest();
                o.setRentalID(rs.getLong(1));
                o.setInsurerCountryID(rs.getLong(2));
                o.setSupplierID(rs.getLong(3));
                o.setInvoiceType(rs.getString(4));
                o.setNetAmount(rs.getBigDecimal(5));
                o.setVatAmount(rs.getBigDecimal(6));
                o.setGrossAmount(rs.getBigDecimal(7));
                o.setUpdated(rs.getTimestamp(8));
                o.setAckSessionID(rs.getLong(9));
                if(rs.wasNull())
                    o.setAckSessionID(-1);
                o.setAckDate(rs.getTimestamp(10));
                if(rs.wasNull())
                    o.setAckDate(null);
                o.setUuid(rs.getString(11));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_payment_request(rental_id,insurer_country_id,supplier_id,invoice_type,net_amount,vat_amount,gross_amount,updated,ack_session_id,ack_date,uuid) values (?,?,?,?,?,?,?,?,?,?,?) returning id");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryID());
            s.setLong(3,getSupplierID());
            s.setString(4,getInvoiceType());
            s.setBigDecimal(5,getNetAmount());
            s.setBigDecimal(6,getVatAmount());
            s.setBigDecimal(7,getGrossAmount());
            s.setTimestamp(8,getUpdated());
            if(getAckSessionID()==-1)
                s.setNull(9,Types.INTEGER);
            else
                s.setLong(9,getAckSessionID());
            if(getAckDate()==null)
                s.setNull(10,Types.TIMESTAMP);
            else
                s.setTimestamp(10,getAckDate());
            s.setString(11,getUuid());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_payment_request set rental_id=?,insurer_country_id=?,supplier_id=?,invoice_type=?,net_amount=?,vat_amount=?,gross_amount=?,updated=?,ack_session_id=?,ack_date=?,uuid=? where id=?");
            s.setLong(1,getRentalID());
            s.setLong(2,getInsurerCountryID());
            s.setLong(3,getSupplierID());
            s.setString(4,getInvoiceType());
            s.setBigDecimal(5,getNetAmount());
            s.setBigDecimal(6,getVatAmount());
            s.setBigDecimal(7,getGrossAmount());
            s.setTimestamp(8,getUpdated());
            if(getAckSessionID()==-1)
                s.setNull(9,Types.INTEGER);
            else
                s.setLong(9,getAckSessionID());
            if(getAckDate()==null)
                s.setNull(10,Types.TIMESTAMP);
            else
                s.setTimestamp(10,getAckDate());
            s.setString(11,getUuid());
            s.setLong(12, getID());
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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
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

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

   
}
