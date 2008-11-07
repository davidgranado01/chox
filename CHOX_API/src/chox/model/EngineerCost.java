/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;


import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EngineerCost {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal netFee;
    private BigDecimal feeVAT;
    private BigDecimal grossFee;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getClaimID() {
        return claimID;
    }

    public void setClaimID(long claimID) {
        this.claimID = claimID;
    }

    public BigDecimal getFeeVAT() {
        return feeVAT;
    }

    public void setFeeVAT(BigDecimal feeVAT) {
        this.feeVAT = feeVAT;
    }

    public BigDecimal getGrossFee() {
        return grossFee;
    }

    public void setGrossFee(BigDecimal grossFee) {
        this.grossFee = grossFee;
    }

    public BigDecimal getNetFee() {
        return netFee;
    }

    public void setNetFee(BigDecimal netFee) {
        this.netFee = netFee;
    }
    
    
}