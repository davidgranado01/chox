package chox.services;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import chox.model.Supplier;

public class supplierServiceImpl {

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
            }
            rs.close();
            s.close();
            Cache.getInstance().put(cacheKey, id, o);
            return o;
        }
    }    
}
