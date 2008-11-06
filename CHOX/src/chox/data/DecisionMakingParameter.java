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
import java.util.HashMap;

public class DecisionMakingParameter {

    private long ID = -1;
    private long decisionID=-1;
    private String name;
    private BigDecimal numericValue;
    private String stringValue;
    private Timestamp dateValue;

    public static HashMap<String,DecisionMakingParameter> getParameters(DBConnectionWrapper connection,DecisionMaking decision) throws SQLException
    {
        HashMap<String,DecisionMakingParameter> l=new HashMap<String,DecisionMakingParameter>();        
        PreparedStatement s=connection.prepareStatement("select id from decision_making_parameter where decision_id=?");
        s.setLong(1,decision.getID());
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            DecisionMakingParameter dmp=instantiate(connection,rs.getLong(1));
            l.put(dmp.getName(), dmp);
        }
        rs.close();
        s.close();        
        return l;
    }

    public static DecisionMakingParameter instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.DecisionMakingParameter.instantiate";
        try {
            return (DecisionMakingParameter) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select decision_id,name,numeric_value,string_value,date_value from decision_making_parameter where id=?");
            s.setLong(1, id);
            DecisionMakingParameter o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new DecisionMakingParameter();
                o.setDecisionID(rs.getLong(1));
                o.setName(rs.getString(2));
                o.setNumericValue(rs.getBigDecimal(3));
                if(rs.wasNull())
                    o.setNumericValue(null);
                o.setStringValue(rs.getString(4));
                if(rs.wasNull())
                    o.setStringValue(null);
                o.setDateValue(rs.getTimestamp(5));
                if(rs.wasNull())
                    o.setDateValue(null);                
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
            PreparedStatement s = connection.prepareStatement("insert into decision_making_parameter(decision_id,name,numeric_value,string_value,date_value) values (?,?,?,?,?) returning id");
            s.setLong(1,getDecisionID());
            s.setString(2,getName());
            if(getNumericValue()==null)
                s.setNull(3,Types.NUMERIC);
            else
                s.setBigDecimal(3,getNumericValue());
            if(getStringValue()==null)
                s.setNull(4,Types.VARCHAR);
            else
                s.setString(4,getStringValue());
            if(getDateValue()==null)
                s.setNull(5,Types.TIMESTAMP);
            else
                s.setTimestamp(5,getDateValue());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update decision_making_parameter set decision_id=?,name=?,numeric_value=?,string_value=?,date_value=? where id=?");
            s.setLong(1,getDecisionID());
            s.setString(2,getName());
            if(getNumericValue()==null)
                s.setNull(3,Types.NUMERIC);
            else
                s.setBigDecimal(3,getNumericValue());
            if(getStringValue()==null)
                s.setNull(4,Types.VARCHAR);
            else
                s.setString(4,getStringValue());
            if(getDateValue()==null)
                s.setNull(5,Types.TIMESTAMP);
            else
                s.setTimestamp(5,getDateValue());
            s.setLong(6, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getDecisionID() {
        return decisionID;
    }

    public void setDecisionID(long decisionID) {
        this.decisionID = decisionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Timestamp getDateValue() {
        return dateValue;
    }

    public void setDateValue(Timestamp dateValue) {
        this.dateValue = dateValue;
    }

   

}
