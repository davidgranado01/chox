package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class Witness {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long claimID = -1;
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String telephoneDay;
    private String telephoneEvening;
    private String email;

    
    public static void removeForClaim(DBConnectionWrapper connection, Claim claim) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from witness where claim_id=?");
        s.setLong(1, claim.getID());
        s.executeUpdate();
        s.close();
    }
    
    public static ArrayList<Witness> getForClaim(DBConnectionWrapper connection,Claim claim) throws SQLException {
        ArrayList<Witness> l=new ArrayList<Witness>();
        PreparedStatement s=connection.prepareStatement("select id from witness where claim_id=?");
        s.setLong(1,claim.getID());
        ResultSet rs=s.executeQuery();
        while(rs.next()) {
            l.add(instantiate(connection,rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }
    
    public static Witness instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Witness.instantiate";
        try {
            return (Witness) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,claim_id,name,address1,address2,address3,address4,address5,postcode,telephone_day,telephone_evening,email from witness where id=?");
            s.setLong(1, id);
            Witness o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Witness();
                o.setUuid(rs.getString(1));
                o.setClaimID(rs.getLong(2));
                o.setName(rs.getString(3));
                o.setAddress1(rs.getString(4));
                if (rs.wasNull()) {
                    o.setAddress1(null);
                }
                o.setAddress2(rs.getString(5));
                if (rs.wasNull()) {
                    o.setAddress2(null);
                }
                o.setAddress3(rs.getString(6));
                if (rs.wasNull()) {
                    o.setAddress3(null);
                }
                o.setAddress4(rs.getString(7));
                if (rs.wasNull()) {
                    o.setAddress4(null);
                }
                o.setAddress5(rs.getString(8));
                if (rs.wasNull()) {
                    o.setAddress5(null);
                }
                o.setPostcode(rs.getString(9));
                if (rs.wasNull()) {
                    o.setPostcode(null);
                }
                o.setTelephoneDay(rs.getString(10));
                if (rs.wasNull()) {
                    o.setTelephoneDay(null);
                }
                o.setTelephoneEvening(rs.getString(11));
                if (rs.wasNull()) {
                    o.setTelephoneEvening(null);
                }
                o.setEmail(rs.getString(12));
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
            PreparedStatement s = connection.prepareStatement("insert into witness(uuid,claim_id,name,address1,address2,address3,address4,address5,postcode,telephone_day,telephone_evening,email) values (?,?,?,?,?,?,?,?,?,?,?,?) returning id");
            s.setString(1, getUuid());
            s.setLong(2, getClaimID());
            s.setString(3, getName());
            if (getAddress1() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getAddress1());
            }
            if (getAddress2() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getAddress2());
            }
            if (getAddress3() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getAddress3());
            }
            if (getAddress4() == null) {
                s.setNull(7, Types.VARCHAR);
            } else {
                s.setString(7, getAddress4());
            }
            if (getAddress5() == null) {
                s.setNull(8, Types.VARCHAR);
            } else {
                s.setString(8, getAddress5());
            }
            if (getPostcode() == null) {
                s.setNull(9, Types.VARCHAR);
            } else {
                s.setString(9, getPostcode());
            }
            if (getTelephoneDay() == null) {
                s.setNull(10, Types.VARCHAR);
            } else {
                s.setString(10, getTelephoneDay());
            }
            if (getTelephoneEvening() == null) {
                s.setNull(11, Types.VARCHAR);
            } else {
                s.setString(11, getTelephoneEvening());
            }
            if (getEmail() == null) {
                s.setNull(12, Types.VARCHAR);
            } else {
                s.setString(12, getEmail());
            }
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update witness set uuid=?,claim_id=?,name=?,address1=?,address2=?,address3=?,address4=?,address5=?,postcode=?,telephone_day=?,telephone_evening=?,email=? where id=?");
            s.setString(1, getUuid());
            s.setLong(2, getClaimID());
            s.setString(3, getName());
            if (getAddress1() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getAddress1());
            }
            if (getAddress2() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getAddress2());
            }
            if (getAddress3() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getAddress3());
            }
            if (getAddress4() == null) {
                s.setNull(7, Types.VARCHAR);
            } else {
                s.setString(7, getAddress4());
            }
            if (getAddress5() == null) {
                s.setNull(8, Types.VARCHAR);
            } else {
                s.setString(8, getAddress5());
            }
            if (getPostcode() == null) {
                s.setNull(9, Types.VARCHAR);
            } else {
                s.setString(9, getPostcode());
            }
            if (getTelephoneDay() == null) {
                s.setNull(10, Types.VARCHAR);
            } else {
                s.setString(10, getTelephoneDay());
            }
            if (getTelephoneEvening() == null) {
                s.setNull(11, Types.VARCHAR);
            } else {
                s.setString(11, getTelephoneEvening());
            }
            if (getEmail() == null) {
                s.setNull(12, Types.VARCHAR);
            } else {
                s.setString(12, getEmail());
            }
            s.setLong(13, getID());
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

    public long getClaimID() {
        return claimID;
    }

    public void setClaimID(long claimID) {
        this.claimID = claimID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTelephoneDay() {
        return telephoneDay;
    }

    public void setTelephoneDay(String telephoneDay) {
        this.telephoneDay = telephoneDay;
    }

    public String getTelephoneEvening() {
        return telephoneEvening;
    }

    public void setTelephoneEvening(String telephoneEvening) {
        this.telephoneEvening = telephoneEvening;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
