package chox.processor;

import chox.data.Claim;
import chox.data.InsurerCountryLocation;
import chox.data.InsurerCountryProduct;
import chox.data.Rental;
import chox.data.RentalInsurerLocation;
import chox.data.RentalInsurerProduct;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CatchAllRentalProcessor extends AbstractRentalProcessor {

    public void process(DBConnectionWrapper connection, Rental rental) throws Exception {
        
        Claim c=Claim.getForRental(connection, rental);
                
        PreparedStatement s=connection.prepareStatement("select id from insurer_country_location where insurer_country_id=? and catch_all='y'");
        s.setLong(1,c.getTpInsurerCountryID());
        ResultSet rs=s.executeQuery();
        InsurerCountryLocation l=null;
        if(rs.next()) {
            l=InsurerCountryLocation.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        
        if(l!=null) {
            RentalInsurerLocation ril=new RentalInsurerLocation();
            ril.setInsurerCountryLocationID(l.getID());
            ril.setRentalID(rental.getID());
            ril.save(connection);
        }
        
        s=connection.prepareStatement("select id from insurer_country_product where insurer_country_id=? and catch_all='y'");
        s.setLong(1,c.getTpInsurerCountryID());
        rs=s.executeQuery();
        InsurerCountryProduct p=null;
        if(rs.next()) {
            p=InsurerCountryProduct.instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();
        
        if(l!=null) {
            RentalInsurerProduct rip=new RentalInsurerProduct();
            rip.setInsurerCountryProductID(p.getID());
            rip.setRentalID(rental.getID());
            rip.save(connection);
        }
        
    }

    
}
