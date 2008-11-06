package chox.util;

import chox.data.CHOXUsageFee;
import chox.data.CHOXUsageRate;
import chox.data.Claim;
import chox.data.InsurerCountry;
import chox.data.Rental;
import chox.data.Supplier;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import idas.web.security.iDASSession;
import java.sql.SQLException;

public class FeeGenerator {

    public static void chargeFee(DBConnectionWrapper connection, iDASSession session, Rental rental, String feeType) throws SQLException {
        Claim c = Claim.getForRental(connection, rental);
        if (c != null) {
            InsurerCountry ic = InsurerCountry.instantiate(connection, c.getTpInsurerCountryID());
            chargeFee(connection,session,rental,feeType,ic);
        }
        Supplier supplier=Supplier.instantiate(connection, rental.getSupplierID());
        chargeFee(connection,session,rental,feeType,supplier);
    }

    public static void chargeFee(DBConnectionWrapper connection, iDASSession session, Rental rental, String feeType, InsurerCountry insurerCountry) throws SQLException {
        CHOXUsageRate r = CHOXUsageRate.getRate(connection, feeType, insurerCountry);
        if (r == null) {
            return;
        }
        CHOXUsageFee f = new CHOXUsageFee();
        f.setFeeType(feeType);
        f.setRentalID(rental.getID());
        f.setSessionID(session.getSessionID());
        f.setNetAmount(r.getNetAmount());
        f.setVatAmount(r.getVatAmount());
        f.setGrossAmount(r.getGrossAmount());
        f.setInsurerCountryID(insurerCountry.getID());
        f.save(connection);
    }

    public static void chargeFee(DBConnectionWrapper connection, iDASSession session, Rental rental, String feeType, Supplier supplier) throws SQLException {
        CHOXUsageRate r = CHOXUsageRate.getRate(connection, feeType, supplier);
        if (r == null) {
            return;
        }
        CHOXUsageFee f = new CHOXUsageFee();
        f.setFeeType(feeType);
        f.setRentalID(rental.getID());
        f.setSessionID(session.getSessionID());
        f.setNetAmount(r.getNetAmount());
        f.setVatAmount(r.getVatAmount());
        f.setGrossAmount(r.getGrossAmount());
        f.setSupplierID(supplier.getID());
        f.save(connection);
    }
}
