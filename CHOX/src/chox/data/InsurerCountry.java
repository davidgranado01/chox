package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class InsurerCountry {

    private long ID = -1;
    private long insurerID = -1;
    private long countryID = -1;
    private String name = "";
    private String address1 = "";
    private String address2 = "";
    private String address3;
    private String address4;
    private String address5;
    private String postcode = "";

    public static ArrayList<InsurerCountry> getAll(DBConnectionWrapper connection) throws SQLException {
        ArrayList<InsurerCountry> l = new ArrayList<InsurerCountry>();

        PreparedStatement s = connection.prepareStatement("select id from insurer_country order by name");
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();

        return l;
    }

    public static InsurerCountry getByName(DBConnectionWrapper connection, String name) throws SQLException {
        PreparedStatement s = connection.prepareStatement("select id from insurer_country where name=?");
        s.setString(1, name);
        ResultSet rs = s.executeQuery();
        InsurerCountry c = null;
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        if (c == null) {
            s = connection.prepareStatement("select id from insurer_country where lower(name)=?");
            s.setString(1, name.toLowerCase().trim());
            rs = s.executeQuery();
            if (rs.next()) {
                c = instantiate(connection, rs.getLong(1));
            }
            rs.close();
            s.close();
        }

        if (c == null) {
            s = connection.prepareStatement("select insurer_country_id from insurer_country_alias where lower(alias)=?");
            s.setString(1, name.toLowerCase().trim());
            rs = s.executeQuery();
            if (rs.next()) {
                c = instantiate(connection, rs.getLong(1));
            }
            rs.close();
            s.close();
        }

        return c;
    }

    public static InsurerCountry instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.InsurerCountry.instantiate";
        try {
            return (InsurerCountry) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode from insurer_country where id=?");
            s.setLong(1, id);
            InsurerCountry o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new InsurerCountry();
                o.setInsurerID(rs.getLong(1));
                o.setCountryID(rs.getLong(2));
                o.setName(rs.getString(3));
                o.setAddress1(rs.getString(4));
                o.setAddress2(rs.getString(5));
                o.setAddress3(rs.getString(6));
                o.setAddress4(rs.getString(7));
                o.setAddress5(rs.getString(8));
                o.setPostcode(rs.getString(9));

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
            PreparedStatement s = connection.prepareStatement("insert into insurer_country(insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode) values (?,?,?,?,?,?,?,?,?) returning id");

            s.setLong(1, getInsurerID());
            s.setLong(2, getCountryID());
            s.setString(3, getName());
            s.setString(4, getAddress1());
            s.setString(5, getAddress2());
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
            s.setString(9, getPostcode());

            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update insurer_country set insurer_id=?,country_id=?,name=?,address1=?,address2=?,address3=?,address4=?,address5=?,postcode=? where id=?");
            s.setLong(1, getInsurerID());
            s.setLong(2, getCountryID());
            s.setString(3, getName());
            s.setString(4, getAddress1());
            s.setString(5, getAddress2());
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
            s.setString(9, getPostcode());

            s.setLong(10, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public long getInsurerID() {
        return insurerID;
    }

    public void setInsurerID(long insurerID) {
        this.insurerID = insurerID;
    }

    public long getCountryID() {
        return countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
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
}
