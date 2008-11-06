package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class EngineerReport {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal labourAmount;
    private BigDecimal totalAmount;
    private BigDecimal days;
    private String usable;
    private String name;
    private String company;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String telephone;
    private String email;

    
    public static EngineerReport getForClaim(DBConnectionWrapper connection,Claim claim) throws SQLException {
        EngineerReport c=null;        
        PreparedStatement s=connection.prepareStatement("select id from engineer_report where claim_id=?");
        s.setLong(1,claim.getID());
        ResultSet rs=s.executeQuery();
        if(rs.next()) {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();        
        return c;
    }    
    
    public static void removeForClaim(DBConnectionWrapper connection, Claim claim) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from engineer_report where claim_id=?");
        s.setLong(1, claim.getID());
        s.executeUpdate();
        s.close();
    }

    public static EngineerReport instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.EngineerReport.instantiate";
        try {
            return (EngineerReport) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select claim_id,labour_amount,total_amount,days,usable,name,company,address1,address2,address3,address4,address5,postcode,telephone,email from engineer_report where id=?");
            s.setLong(1, id);
            EngineerReport o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new EngineerReport();
                o.setClaimID(rs.getLong(1));
                o.setLabourAmount(rs.getBigDecimal(2));
                o.setTotalAmount(rs.getBigDecimal(3));
                o.setDays(rs.getBigDecimal(4));
                o.setUsable(rs.getString(5));
                o.setName(rs.getString(6));
                if (rs.wasNull()) {
                    o.setName(null);
                }
                o.setCompany(rs.getString(7));
                if (rs.wasNull()) {
                    o.setCompany(null);
                }
                o.setAddress1(rs.getString(8));
                if (rs.wasNull()) {
                    o.setAddress1(null);
                }
                o.setAddress2(rs.getString(9));
                if (rs.wasNull()) {
                    o.setAddress2(null);
                }
                o.setAddress3(rs.getString(10));
                if (rs.wasNull()) {
                    o.setAddress3(null);
                }
                o.setAddress4(rs.getString(11));
                if (rs.wasNull()) {
                    o.setAddress4(null);
                }
                o.setAddress5(rs.getString(12));
                if (rs.wasNull()) {
                    o.setAddress5(null);
                }
                o.setPostcode(rs.getString(13));
                if (rs.wasNull()) {
                    o.setPostcode(null);
                }
                o.setTelephone(rs.getString(14));
                if (rs.wasNull()) {
                    o.setTelephone(null);
                }
                o.setEmail(rs.getString(15));
                if (rs.wasNull()) {
                    o.setEmail(null);
                }
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
            PreparedStatement s = connection.prepareStatement("insert into engineer_report(claim_id,labour_amount,total_amount,days,usable,name,company,address1,address2,address3,address4,address5,postcode,telephone,email) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning id");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getLabourAmount());
            s.setBigDecimal(3, getTotalAmount());
            s.setBigDecimal(4, getDays());
            s.setString(5, getUsable());
            
            if (getName() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getName());
            }

            if (getCompany() == null) {
                s.setNull(7, Types.VARCHAR);
            } else {
                s.setString(7, getCompany());
            }

            if (getAddress1() == null) {
                s.setNull(8, Types.VARCHAR);
            } else {
                s.setString(8, getAddress1());
            }
            if (getAddress2() == null) {
                s.setNull(9, Types.VARCHAR);
            } else {
                s.setString(9, getAddress1());
            }
            if (getAddress3() == null) {
                s.setNull(10, Types.VARCHAR);
            } else {
                s.setString(10, getAddress1());
            }
            if (getAddress4() == null) {
                s.setNull(11, Types.VARCHAR);
            } else {
                s.setString(11, getAddress1());
            }
            if (getAddress5() == null) {
                s.setNull(12, Types.VARCHAR);
            } else {
                s.setString(12, getAddress1());
            }
            if (getPostcode() == null) {
                s.setNull(13, Types.VARCHAR);
            } else {
                s.setString(13, getPostcode());
            }
            if (getTelephone() == null) {
                s.setNull(14, Types.VARCHAR);
            } else {
                s.setString(14, getPostcode());
            }
            if (getEmail() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getEmail());
            }
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update engineer_report set claim_id=?,labour_amount=?,total_amount=?,days=?,usable=?,name=?,company=?,address1=?,address2=?,address3=?,address4=?,address5=?,postcode=?,telephone=?,email=? where id=?");
            s.setLong(1, getClaimID());
            s.setBigDecimal(2, getLabourAmount());
            s.setBigDecimal(3, getTotalAmount());
            s.setBigDecimal(4, getDays());
            s.setString(5, getUsable());
            
            if (getName() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getName());
            }

            if (getCompany() == null) {
                s.setNull(7, Types.VARCHAR);
            } else {
                s.setString(7, getCompany());
            }

            if (getAddress1() == null) {
                s.setNull(8, Types.VARCHAR);
            } else {
                s.setString(8, getAddress1());
            }
            if (getAddress2() == null) {
                s.setNull(9, Types.VARCHAR);
            } else {
                s.setString(9, getAddress1());
            }
            if (getAddress3() == null) {
                s.setNull(10, Types.VARCHAR);
            } else {
                s.setString(10, getAddress1());
            }
            if (getAddress4() == null) {
                s.setNull(11, Types.VARCHAR);
            } else {
                s.setString(11, getAddress1());
            }
            if (getAddress5() == null) {
                s.setNull(12, Types.VARCHAR);
            } else {
                s.setString(12, getAddress1());
            }
            if (getPostcode() == null) {
                s.setNull(13, Types.VARCHAR);
            } else {
                s.setString(13, getPostcode());
            }
            if (getTelephone() == null) {
                s.setNull(14, Types.VARCHAR);
            } else {
                s.setString(14, getPostcode());
            }
            if (getEmail() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getEmail());
            }
            s.setLong(16, getID());
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

    public BigDecimal getLabourAmount() {
        return labourAmount;
    }

    public void setLabourAmount(BigDecimal labourAmount) {
        this.labourAmount = labourAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
