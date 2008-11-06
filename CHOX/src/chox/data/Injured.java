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

public class Injured {

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
    private String solicitorAppointed = "n";
    private String solicitorName;
    private String solicitorAddress1;
    private String solicitorAddress2;
    private String solicitorAddress3;
    private String solicitorAddress4;
    private String solicitorAddress5;
    private String solicitorPostcode;
    private String solicitorTelephone;
    private String solicitorEmail;

    public static ArrayList<Injured> getForClaim(DBConnectionWrapper connection, Claim claim) throws SQLException {
        ArrayList<Injured> l = new ArrayList<Injured>();
        PreparedStatement s = connection.prepareStatement("select id from injured where claim_id=?");
        s.setLong(1, claim.getID());
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();
        return l;
    }

    public static void removeForClaim(DBConnectionWrapper connection, Claim claim) throws SQLException {
        PreparedStatement s = connection.prepareStatement("delete from injured where claim_id=?");
        s.setLong(1, claim.getID());
        s.executeUpdate();
        s.close();
    }

    public static Injured instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Injured.instantiate";
        try {
            return (Injured) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,claim_id,name,address1,address2,address3,address4,address5,postcode,telephone_day,telephone_evening,email,solicitor_appointed,solicitor_name,solicitor_address1,solicitor_address2,solicitor_address3,solicitor_address4,solicitor_address5,solicitor_postcode,solicitor_telephone,solicitor_email from injured where id=?");
            s.setLong(1, id);
            Injured o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Injured();
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

                o.setSolicitorAppointed(rs.getString(13));

                o.setSolicitorName(rs.getString(14));
                o.setSolicitorAddress1(rs.getString(15));
                if (rs.wasNull()) {
                    o.setSolicitorAddress1(null);
                }
                o.setSolicitorAddress2(rs.getString(16));
                if (rs.wasNull()) {
                    o.setSolicitorAddress2(null);
                }
                o.setSolicitorAddress3(rs.getString(17));
                if (rs.wasNull()) {
                    o.setSolicitorAddress3(null);
                }
                o.setSolicitorAddress4(rs.getString(18));
                if (rs.wasNull()) {
                    o.setSolicitorAddress4(null);
                }
                o.setSolicitorAddress5(rs.getString(19));
                if (rs.wasNull()) {
                    o.setSolicitorAddress5(null);
                }
                o.setSolicitorPostcode(rs.getString(20));
                if (rs.wasNull()) {
                    o.setSolicitorPostcode(null);
                }
                o.setSolicitorTelephone(rs.getString(21));
                if (rs.wasNull()) {
                    o.setSolicitorTelephone(null);
                }
                o.setSolicitorEmail(rs.getString(22));
                if (rs.wasNull()) {
                    o.setSolicitorEmail(null);
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
            PreparedStatement s = connection.prepareStatement("insert into injured(uuid,claim_id,name,address1,address2,address3,address4,address5,postcode,telephone_day,telephone_evening,email,solicitor_appointed,solicitor_name,solicitor_address1,solicitor_address2,solicitor_address3,solicitor_address4,solicitor_address5,solicitor_postcode,solicitor_telephone,solicitor_email) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning id");
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

            s.setString(13, getSolicitorAppointed());

            s.setString(14, getSolicitorName());
            if (getSolicitorAddress1() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getSolicitorAddress1());
            }
            if (getSolicitorAddress2() == null) {
                s.setNull(16, Types.VARCHAR);
            } else {
                s.setString(16, getSolicitorAddress2());
            }
            if (getSolicitorAddress3() == null) {
                s.setNull(17, Types.VARCHAR);
            } else {
                s.setString(17, getSolicitorAddress3());
            }
            if (getSolicitorAddress4() == null) {
                s.setNull(18, Types.VARCHAR);
            } else {
                s.setString(18, getSolicitorAddress4());
            }
            if (getSolicitorAddress5() == null) {
                s.setNull(19, Types.VARCHAR);
            } else {
                s.setString(19, getSolicitorAddress5());
            }
            if (getSolicitorPostcode() == null) {
                s.setNull(20, Types.VARCHAR);
            } else {
                s.setString(20, getSolicitorPostcode());
            }
            if (getSolicitorTelephone() == null) {
                s.setNull(21, Types.VARCHAR);
            } else {
                s.setString(21, getSolicitorTelephone());
            }
            if (getSolicitorEmail() == null) {
                s.setNull(22, Types.VARCHAR);
            } else {
                s.setString(22, getSolicitorEmail());
            }

            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update injured set uuid=?,claim_id=?,name=?,address1=?,address2=?,address3=?,address4=?,address5=?,postcode=?,telephone_day=?,telephone_evening=?,email=?,solicitor_appointed=?,solicitor_name=?,solicitor_address1=?,solicitor_address2=?,solicitor_address3=?,solicitor_address4=?,solicitor_address5=?,solicitor_postcode=?,solicitor_telephone=?,solicitor_email=? where id=?");

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

            s.setString(13, getSolicitorAppointed());

            s.setString(14, getSolicitorName());
            if (getSolicitorAddress1() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getSolicitorAddress1());
            }
            if (getSolicitorAddress2() == null) {
                s.setNull(16, Types.VARCHAR);
            } else {
                s.setString(16, getSolicitorAddress2());
            }
            if (getSolicitorAddress3() == null) {
                s.setNull(17, Types.VARCHAR);
            } else {
                s.setString(17, getSolicitorAddress3());
            }
            if (getSolicitorAddress4() == null) {
                s.setNull(18, Types.VARCHAR);
            } else {
                s.setString(18, getSolicitorAddress4());
            }
            if (getSolicitorAddress5() == null) {
                s.setNull(19, Types.VARCHAR);
            } else {
                s.setString(19, getSolicitorAddress5());
            }
            if (getSolicitorPostcode() == null) {
                s.setNull(20, Types.VARCHAR);
            } else {
                s.setString(20, getSolicitorPostcode());
            }
            if (getSolicitorTelephone() == null) {
                s.setNull(21, Types.VARCHAR);
            } else {
                s.setString(21, getSolicitorTelephone());
            }
            if (getSolicitorEmail() == null) {
                s.setNull(22, Types.VARCHAR);
            } else {
                s.setString(22, getSolicitorEmail());
            }

            s.setLong(23, getID());
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

    public String getSolicitorAppointed() {
        return solicitorAppointed;
    }

    public void setSolicitorAppointed(String solicitorAppointed) {
        this.solicitorAppointed = solicitorAppointed;
    }

    public String getSolicitorName() {
        return solicitorName;
    }

    public void setSolicitorName(String solicitorName) {
        this.solicitorName = solicitorName;
    }

    public String getSolicitorAddress1() {
        return solicitorAddress1;
    }

    public void setSolicitorAddress1(String solicitorAddress1) {
        this.solicitorAddress1 = solicitorAddress1;
    }

    public String getSolicitorAddress2() {
        return solicitorAddress2;
    }

    public void setSolicitorAddress2(String solicitorAddress2) {
        this.solicitorAddress2 = solicitorAddress2;
    }

    public String getSolicitorAddress3() {
        return solicitorAddress3;
    }

    public void setSolicitorAddress3(String solicitorAddress3) {
        this.solicitorAddress3 = solicitorAddress3;
    }

    public String getSolicitorAddress4() {
        return solicitorAddress4;
    }

    public void setSolicitorAddress4(String solicitorAddress4) {
        this.solicitorAddress4 = solicitorAddress4;
    }

    public String getSolicitorAddress5() {
        return solicitorAddress5;
    }

    public void setSolicitorAddress5(String solicitorAddress5) {
        this.solicitorAddress5 = solicitorAddress5;
    }

    public String getSolicitorPostcode() {
        return solicitorPostcode;
    }

    public void setSolicitorPostcode(String solicitorPostcode) {
        this.solicitorPostcode = solicitorPostcode;
    }

    public String getSolicitorTelephone() {
        return solicitorTelephone;
    }

    public void setSolicitorTelephone(String solicitorTelephone) {
        this.solicitorTelephone = solicitorTelephone;
    }

    public String getSolicitorEmail() {
        return solicitorEmail;
    }

    public void setSolicitorEmail(String solicitorEmail) {
        this.solicitorEmail = solicitorEmail;
    }
}
