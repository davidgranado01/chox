package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class RentalProcessor {

    private long ID = -1;
    private long tpInsurerCountryID = -1;
    private long supplierID=-1;
    private int seq=0;
    private String javaClass;

    public static ArrayList<RentalProcessor> getForRental(DBConnectionWrapper connection,Rental rental) throws SQLException
    {
        ArrayList<RentalProcessor> l=new ArrayList<RentalProcessor>();
        Claim c=Claim.getForRental(connection, rental);
        
        PreparedStatement s=connection.prepareStatement("select id from rental_processor where tp_insurer_country_id=? or supplier_id=? order by seq");
        s.setLong(1,c.getTpInsurerCountryID());
        s.setLong(2,rental.getSupplierID());
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();
        
        return l;
    }
    
    public static RentalProcessor instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RentalProcessor.instantiate";
        try {
            return (RentalProcessor) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select tp_insurer_country_id,supplier_id,seq,java_class from rental_processor where id=?");
            s.setLong(1, id);
            RentalProcessor o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RentalProcessor();
                o.setTpInsurerCountryID(rs.getLong(1));
                if(rs.wasNull())
                    o.setTpInsurerCountryID(-1);
                o.setSupplierID(rs.getLong(2));
                if(rs.wasNull())
                    o.setSupplierID(-1);
                o.setSeq(rs.getInt(3));
                o.setJavaClass(rs.getString(4));
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
            PreparedStatement s = connection.prepareStatement("insert into rental_processor(tp_insurer_country_id,supplier_id,seq,java_class) values (?,?,?,?) returning id");
            if(getTpInsurerCountryID()==-1)
                s.setNull(1,Types.INTEGER);
            else
                s.setLong(1,getTpInsurerCountryID());
            if(getSupplierID()==-1)
                s.setNull(2,Types.INTEGER);
            else
                s.setLong(2,getSupplierID());
            s.setInt(3,getSeq());
            s.setString(4,getJavaClass());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update rental_processor set tp_insurer_country_id=?,supplier_id=?,seq=?,java_class=? where id=?");
            if(getTpInsurerCountryID()==-1)
                s.setNull(1,Types.INTEGER);
            else
                s.setLong(1,getTpInsurerCountryID());
            if(getSupplierID()==-1)
                s.setNull(2,Types.INTEGER);
            else
                s.setLong(2,getSupplierID());
            s.setInt(3,getSeq());
            s.setString(4,getJavaClass());
            s.setLong(5, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getTpInsurerCountryID() {
        return tpInsurerCountryID;
    }

    public void setTpInsurerCountryID(long tpInsurerCountryID) {
        this.tpInsurerCountryID = tpInsurerCountryID;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    

    
}
