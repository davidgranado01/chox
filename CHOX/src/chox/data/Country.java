package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Country {

    private long ID = -1;
    private String name="";
    private String currency="";
    private BigDecimal vatRate=new BigDecimal(0);
    
    
    public static Country getCountry(DBConnectionWrapper connection,String name) throws SQLException {
        Country c=null;
        PreparedStatement s=connection.prepareStatement("select id from country where name=?");
        s.setString(1,name);
        ResultSet rs=s.executeQuery();
        if(rs.next())   {
            c=instantiate(connection,rs.getLong(1));
        }
        rs.close();
        s.close();
        return c;
    }
    
    public static Country instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Country.instantiate";
        try {
            return (Country) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select name,currency,vat_rate from country where id=?");
            s.setLong(1, id);
            Country o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Country();
                o.setName(rs.getString(1));
                o.setCurrency(rs.getString(2));
                o.setVatRate(rs.getBigDecimal(3));
                o.ID=id;
            }
            rs.close();
            s.close();
            Cache.getInstance().put(cacheKey, id, o);
            return o;
        }
    }
    
    public void save(DBConnectionWrapper connection) throws SQLException {
        if (getID() == -1) {
            PreparedStatement s = connection.prepareStatement("insert into country(name,currency,vat_rate) values (?,?,?) returning id");
            s.setString(1,getName());
            s.setString(2,getCurrency());
            s.setBigDecimal(3,getVatRate());            
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update country set name=?,currency=?,vat_rate=? where id=?");
            s.setString(1,getName());
            s.setString(2,getCurrency());
            s.setBigDecimal(3,getVatRate());
            s.setLong(4,getID());
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }


    


    

}
