package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Insurer {

    private long ID = -1;
    private String name = "";

    public static Insurer getInsurer(DBConnectionWrapper connection, String name) throws SQLException {
        Insurer c = null;
        PreparedStatement s = connection.prepareStatement("select id from insurer where name=?");
        s.setString(1, name);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        return c;
    }

    public InsurerCountry getInsurerCountry(DBConnectionWrapper connection, Country country, String name)
            throws SQLException {
        PreparedStatement s = connection.prepareStatement("select id from insurer_country where name=? and insurer_id=? and country_id=?");
        s.setString(1, name);
        s.setLong(2, getID());
        s.setLong(3, country.getID());
        ResultSet rs = s.executeQuery();
        InsurerCountry c = null;
        if (rs.next()) {
            c = InsurerCountry.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        return c;
    }

    public InsurerCountry getInsurerCountry(DBConnectionWrapper connection, Country country)
            throws SQLException {
        PreparedStatement s = connection.prepareStatement("select id from insurer_country where insurer_id=? and country_id=?");
        s.setLong(1, getID());
        s.setLong(2, country.getID());
        ResultSet rs = s.executeQuery();
        InsurerCountry c = null;
        if (rs.next()) {
            c = InsurerCountry.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        return c;
    }

    public static Insurer instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Insurer.instantiate";
        try {
            return (Insurer) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select name from idas_insurer where id=?");
            s.setLong(1, id);
            Insurer o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Insurer();
                o.setName(rs.getString(1));
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
            PreparedStatement s = connection.prepareStatement("insert into insurer(name) values (?) returning id");
            s.setString(1, getName());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update insurer set name=? where id=?");
            s.setString(1, getName());
            s.setLong(3, getID());
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
}
