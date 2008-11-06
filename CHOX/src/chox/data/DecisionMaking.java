package chox.data;

import chox.decision.Decision;
import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DecisionMaking {

    private long ID = -1;
    private String decisionType;
    private long supplierID = -1;
    private long insurerCountryID = -1;
    private String javaClass;
    private String autoAuthorise="n";

    public static Decision getDecisionForRental(DBConnectionWrapper connection, Rental rental, String decisionType) throws Exception {
        DecisionMaking dm = getForRental(connection, rental, decisionType);
        if (dm == null) {
            return null;
        }
        Decision d = null;
        try {
            d = (Decision) Class.forName(dm.getJavaClass()).newInstance();
        } catch (Exception e) {
            Logger.err.println(e.getMessage());
            e.printStackTrace(Logger.err);
        }
        return d;
    }

    public static DecisionMaking getForRental(DBConnectionWrapper connection, Rental rental, String decisionType) throws SQLException {
        DecisionMaking d = null;
        
        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            return null;
        }
        PreparedStatement s = connection.prepareStatement("select id from decision_making where decision_type=? and supplier_id=? and insurer_country_id=?");
        s.setString(1, decisionType);
        s.setLong(2, rental.getSupplierID());
        s.setLong(3, claim.getTpInsurerCountryID());
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            d = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return d;
    }

    public static DecisionMaking instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.DecisionMaking.instantiate";
        try {
            return (DecisionMaking) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select decision_type,supplier_id,insurer_country_id,java_class,auto_authorise from decision_making where id=?");
            s.setLong(1, id);
            DecisionMaking o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new DecisionMaking();
                o.setDecisionType(rs.getString(1));
                o.setSupplierID(rs.getLong(2));
                o.setInsurerCountryID(rs.getLong(3));
                o.setJavaClass(rs.getString(4));
                o.setAutoAuthorise(rs.getString(5));
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
            PreparedStatement s = connection.prepareStatement("insert into decision_making(decision_type,supplier_id,insurer_country_id,java_class,auto_authorise) values (?,?,?,?,?) returning id");
            s.setString(1, getDecisionType());
            s.setLong(2, getSupplierID());
            s.setLong(3, getInsurerCountryID());
            s.setString(4, getJavaClass());
            s.setString(5,getAutoAuthorise());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update decision_making set decision_type=?,supplier_id=?,insurer_country_id=?,java_class=?,auto_authorise where id=?");
            s.setString(1, getDecisionType());
            s.setLong(2, getSupplierID());
            s.setLong(3, getInsurerCountryID());
            s.setString(4, getJavaClass());
            s.setString(5,getAutoAuthorise());
            s.setLong(6, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public long getInsurerCountryID() {
        return insurerCountryID;
    }

    public void setInsurerCountryID(long insurerCountryID) {
        this.insurerCountryID = insurerCountryID;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public String getAutoAuthorise() {
        return autoAuthorise;
    }

    public void setAutoAuthorise(String autoAuthorise) {
        this.autoAuthorise = autoAuthorise;
    }
}
