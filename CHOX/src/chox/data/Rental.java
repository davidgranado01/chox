package chox.data;

import chox.processor.AbstractRentalProcessor;
import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Rental {

    public static final String PENDING="Pending";
    public static final String IN_PROGRESS="InProgress";
    public static final String COMPLETE="Complete";
    public static final String CANCELLED="Cancelled";
    
    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long supplierID = -1;
    private String supplierReference;
    private String rentalStatus = "In Progress";
    private Timestamp firstContact = new Timestamp(System.currentTimeMillis());
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    
    
    public void updateInsurerLocationProduct(DBConnectionWrapper connection) throws Exception
    {
        PreparedStatement s=connection.prepareStatement("delete from rental_insurer_location where rental_id=?");
        s.setLong(1,ID);
        s.executeUpdate();
        s.close();
        
        s=connection.prepareStatement("delete from rental_insurer_product where rental_id=?");
        s.setLong(1,ID);
        s.executeUpdate();
        s.close();
        
        ArrayList<RentalProcessor> rps=RentalProcessor.getForRental(connection, this);
        for(RentalProcessor rp:rps) {
            Class c=Class.forName(rp.getJavaClass());
            AbstractRentalProcessor arp=(AbstractRentalProcessor)c.newInstance();
            arp.process(connection, this);
        }        
        
        RentalInsurerLocation l=RentalInsurerLocation.getForRental(connection, this);
        if(l==null) {
            throw new Exception("No location available for rental ["+ID+"]");
        }
        RentalInsurerProduct p=RentalInsurerProduct.getForRental(connection, this);
        if(p==null) {
            throw new Exception("No product available for rental ["+ID+"]");
        }
        
    }
    
    public void updateSearchData(DBConnectionWrapper connection) throws SQLException
    {
        PreparedStatement s=connection.prepareStatement("delete from search_materialised where rental_id=?");
        s.setLong(1,ID);
        s.executeUpdate();
        s.close();
        
        s=connection.prepareStatement("insert into search_materialised(rental_id,field_id,string_value,lcase_string_value,integer_value,date_value,numeric_value) select distinct rental_id,field_id,string_value,lower(string_value),integer_value,date_value,numeric_value from search_source where rental_id=?");
        s.setLong(1,ID);
        s.executeUpdate();
        s.close();        
    }

    public static Rental getByUUID(DBConnectionWrapper connection, String uuid) throws SQLException {
        Rental c = null;

        PreparedStatement s = connection.prepareStatement("select id from rental where uuid=?");
        s.setString(1, uuid);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }

    public static Rental getByReference(DBConnectionWrapper connection, Supplier supplier, String reference) throws SQLException {
        Rental c = null;

        PreparedStatement s = connection.prepareStatement("select id from rental where supplier_id=? and supplier_reference=?");
        s.setLong(1, supplier.getID());
        s.setString(2, reference);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }

    public static Rental instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Rental.instantiate";
        try {
            return (Rental) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,supplier_id,supplier_reference,rental_status,first_contact,created from rental where id=?");
            s.setLong(1, id);
            Rental o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Rental();
                o.setUuid(rs.getString(1));
                o.setSupplierID(rs.getLong(2));
                o.setSupplierReference(rs.getString(3));
                o.setRentalStatus(rs.getString(4));
                o.setFirstContact(rs.getTimestamp(5));
                o.setCreated(rs.getTimestamp(6));
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
            PreparedStatement s = connection.prepareStatement("insert into rental(uuid,supplier_id,supplier_reference,rental_status,first_contact,created) values (?,?,?,?,?,?) returning id");
            s.setString(1, getUuid());
            s.setLong(2, getSupplierID());
            s.setString(3, getSupplierReference());
            s.setString(4, getRentalStatus());
            s.setTimestamp(5, getFirstContact());
            s.setTimestamp(6, getCreated());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental set uuid=?,supplier_id=?,supplier_reference=?,rental_status=?,first_contact=?,created=? where id=?");
            s.setString(1, getUuid());
            s.setLong(2, getSupplierID());
            s.setString(3, getSupplierReference());
            s.setString(4, getRentalStatus());
            s.setTimestamp(5, getFirstContact());
            s.setTimestamp(6, getCreated());
            s.setLong(7, getID());
            s.executeUpdate();
            s.close();
        }
        updateSearchData(connection);
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

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Timestamp getFirstContact() {
        return firstContact;
    }

    public void setFirstContact(Timestamp firstContact) {
        this.firstContact = firstContact;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
