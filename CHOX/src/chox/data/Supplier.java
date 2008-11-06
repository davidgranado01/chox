package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class Supplier {

    private long ID = -1;
    private String name = "";
    private String address1 = "";
    private String address2 = "";
    private String address3;
    private String address4;
    private String address5;
    private String postcode = "";
    private long countryID = -1;
    private String vatNo = "";
    private String companyNo = "";

    public static ArrayList<Supplier> getAll(DBConnectionWrapper connection) throws SQLException {
        ArrayList<Supplier> l = new ArrayList<Supplier>();

        PreparedStatement s = connection.prepareStatement("select id from supplier order by name");
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            l.add(instantiate(connection, rs.getLong(1)));
        }
        rs.close();
        s.close();

        return l;
    }

    public static Supplier getByName(DBConnectionWrapper connection, String name) throws SQLException {
        Supplier c = null;

        PreparedStatement s = connection.prepareStatement("select id from supplier where name=?");
        s.setString(1, name);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        if (c == null) {
            s = connection.prepareStatement("select id from supplier where lower(name)=?");
            s.setString(1, name.toLowerCase().trim());
            rs = s.executeQuery();
            if (rs.next()) {
                c = instantiate(connection, rs.getLong(1));
            }
            rs.close();
            s.close();
        }

        if (c == null) {
            s = connection.prepareStatement("select supplier_id from supplier_alias where lower(alias)=?");
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

    public static Supplier instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Supplier.instantiate";
        try {
            return (Supplier) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select name,address1,address2,address3,address4,address5,postcode,country_id,vat_no,company_no from supplier where id=?");
            s.setLong(1, id);
            Supplier o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Supplier();

                o.setName(rs.getString(1));
                o.setAddress1(rs.getString(2));
                o.setAddress2(rs.getString(3));
                o.setAddress3(rs.getString(4));
                o.setAddress4(rs.getString(5));
                o.setAddress5(rs.getString(6));
                o.setPostcode(rs.getString(7));
                o.setCountryID(rs.getLong(8));
                o.setVatNo(rs.getString(9));
                o.setCompanyNo(rs.getString(10));
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
            PreparedStatement s = connection.prepareStatement("insert into supplier(name,address1,address2,address3,address4,address5,postcode,country_id,vat_no,company_no) values (?,?,?,?,?,?,?,?,?,?) returning id");

            s.setString(1, getName());
            s.setString(2, getAddress1());
            s.setString(3, getAddress2());
            if (getAddress3() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getAddress3());
            }
            if (getAddress4() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getAddress4());
            }
            if (getAddress5() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getAddress5());
            }
            s.setString(7, getPostcode());
            s.setLong(8, getCountryID());
            s.setString(9, getVatNo());
            s.setString(10, getCompanyNo());

            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update supplier set name=?,address1=?,address2=?,address3=?,address4=?,address5=?,postcode=?,country_id=?,vat_no=?,company_no=? where id=?");
            s.setString(1, getName());
            s.setString(2, getAddress1());
            s.setString(3, getAddress2());
            if (getAddress3() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getAddress3());
            }
            if (getAddress4() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getAddress4());
            }
            if (getAddress5() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getAddress5());
            }
            s.setString(7, getPostcode());
            s.setLong(8, getCountryID());
            s.setString(9, getVatNo());
            s.setString(10, getCompanyNo());
            s.setLong(11, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
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

    public long getCountryID() {
        return countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }
}
