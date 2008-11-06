package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StorageRecoveryCost {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal netStorageRecovery;
    private BigDecimal storageRecoveryVAT;
    private BigDecimal grossStorageRecovery;

    
    public static StorageRecoveryCost getForClaim(DBConnectionWrapper connection,Claim claim) throws SQLException {
        StorageRecoveryCost c=null;        
        PreparedStatement s=connection.prepareStatement("select id from storage_recovery_cost where claim_id=?");
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
        PreparedStatement s=connection.prepareStatement("delete from storage_recovery_cost where claim_id=?");
        s.setLong(1,claim.getID());
        s.executeUpdate();
        s.close();        
    }
    
    public static StorageRecoveryCost instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.StorageRecoveryCost.instantiate";
        try {
            return (StorageRecoveryCost) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select claim_id,net_storage_recovery,storage_recovery_vat,gross_storage_recovery from storage_recovery_cost where id=?");
            s.setLong(1, id);
            StorageRecoveryCost o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new StorageRecoveryCost();
                o.setClaimID(rs.getLong(1));
                o.setNetStorageRecovery(rs.getBigDecimal(2));
                o.setStorageRecoveryVAT(rs.getBigDecimal(3));
                o.setGrossStorageRecovery(rs.getBigDecimal(4));
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
            PreparedStatement s = connection.prepareStatement("insert into storage_recovery_cost(claim_id,net_storage_recovery,storage_recovery_vat,gross_storage_recovery) values (?,?,?,?) returning id");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getNetStorageRecovery());
            s.setBigDecimal(3, getStorageRecoveryVAT());
            s.setBigDecimal(4, getGrossStorageRecovery());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update storage_recovery_cost set claim_id=?,net_storage_recovery=?,storage_recovery_vat=?,gross_storage_recovery=? where id=?");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getNetStorageRecovery());
            s.setBigDecimal(3, getStorageRecoveryVAT());
            s.setBigDecimal(4, getGrossStorageRecovery());
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

    public BigDecimal getNetStorageRecovery() {
        return netStorageRecovery;
    }

    public void setNetStorageRecovery(BigDecimal netStorageRecovery) {
        this.netStorageRecovery = netStorageRecovery;
    }

    public BigDecimal getStorageRecoveryVAT() {
        return storageRecoveryVAT;
    }

    public void setStorageRecoveryVAT(BigDecimal storageRecoveryVAT) {
        this.storageRecoveryVAT = storageRecoveryVAT;
    }

    public BigDecimal getGrossStorageRecovery() {
        return grossStorageRecovery;
    }

    public void setGrossStorageRecovery(BigDecimal grossStorageRecovery) {
        this.grossStorageRecovery = grossStorageRecovery;
    }

    
}
