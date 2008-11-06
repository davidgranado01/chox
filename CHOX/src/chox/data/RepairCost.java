package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RepairCost {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal netRepair;
    private BigDecimal repairVAT;
    private BigDecimal grossRepair;

    
    public static RepairCost getForClaim(DBConnectionWrapper connection,Claim claim) throws SQLException {
        RepairCost c=null;        
        PreparedStatement s=connection.prepareStatement("select id from repair_cost where claim_id=?");
        s.setLong(1,claim.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();        
        return c;
    }    
    
    public static void removeForClaim(DBConnectionWrapper connection,Claim claim) throws SQLException {
        PreparedStatement s=connection.prepareStatement("delete from repair_cost where claim_id=?");
        s.setLong(1,claim.getID());
        s.executeUpdate();
        s.close();        
    }
    
    public static RepairCost instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.RepairCost.instantiate";
        try {
            return (RepairCost) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select claim_id,net_repair,repair_vat,gross_repair from repair_cost where id=?");
            s.setLong(1, id);
            RepairCost o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new RepairCost();
                o.setClaimID(rs.getLong(1));
                o.setNetRepair(rs.getBigDecimal(2));
                o.setRepairVAT(rs.getBigDecimal(3));
                o.setGrossRepair(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into repair_cost(claim_id,net_repair,repair_vat,gross_repair) values (?,?,?,?) returning id");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getNetRepair());
            s.setBigDecimal(3, getRepairVAT());
            s.setBigDecimal(4, getGrossRepair());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update repair_cost set claim_id=?,net_repair=?,repair_vat=?,gross_repair=? where id=?");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getNetRepair());
            s.setBigDecimal(3, getRepairVAT());
            s.setBigDecimal(4, getGrossRepair());
            s.setLong(5, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getClaimID() {
        return claimID;
    }

    public void setClaimID(long claimID) {
        this.claimID = claimID;
    }

    public BigDecimal getNetRepair() {
        return netRepair;
    }

    public void setNetRepair(BigDecimal netRepair) {
        this.netRepair = netRepair;
    }

    public BigDecimal getRepairVAT() {
        return repairVAT;
    }

    public void setRepairVAT(BigDecimal repairVAT) {
        this.repairVAT = repairVAT;
    }

    public BigDecimal getGrossRepair() {
        return grossRepair;
    }

    public void setGrossRepair(BigDecimal grossRepair) {
        this.grossRepair = grossRepair;
    }

    
}
