package chox.decision;

import chox.data.Claim;
import chox.data.DecisionMaking;
import chox.data.DecisionMakingParameter;
import chox.data.EngineerReport;
import chox.data.Extra;
import chox.data.InsurerCountry;
import chox.data.Invoice;
import chox.data.Rental;
import chox.data.RentalCost;
import chox.data.RentalExtra;
import chox.data.RentalExtraRate;
import chox.data.RentalRate;
import chox.data.RentalVehicle;
import chox.data.Supplier;
import chox.data.VehicleClass;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class PaySimple extends Decision {

    @Override
    public Boolean decide(DBConnectionWrapper connection, Rental rental) throws Exception {

        DecisionMaking dm = DecisionMaking.getForRental(connection, rental, "invoice pay");
        if (dm == null) {
            return null;
        }
        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            return null;
        }
        Supplier supplier = Supplier.instantiate(connection, rental.getSupplierID());
        InsurerCountry insurerCountry = InsurerCountry.instantiate(connection, claim.getTpInsurerCountryID());

        Invoice invoice = Invoice.getForRental(connection, rental);
        if (invoice == null) {
            return null;
        }
        EngineerReport engineerReport = EngineerReport.getForClaim(connection, claim);
        if (engineerReport == null) {
            return null;
        }
        HashMap<String, DecisionMakingParameter> parameters = DecisionMakingParameter.getParameters(connection, dm);

        BigDecimal invoiceNet = invoice.getNet();
        BigDecimal engineerNet = engineerReport.getTotalAmount();

        // if invoice is less than engineer's estimate
        if (invoiceNet.doubleValue() < engineerNet.doubleValue()) {
            return Boolean.TRUE;
        }
        ArrayList<RentalVehicle> rvl = RentalVehicle.getForRental(connection, rental);

        BigDecimal estimatedRentalFee = new BigDecimal("0.00");
        BigDecimal rentalDays = new BigDecimal("0.00");
        for (RentalVehicle rv : rvl) {
            rentalDays = rentalDays.add(rv.getDays());
            VehicleClass vc = VehicleClass.instantiate(connection, rv.getVehicleClassID());
            RentalRate rr = RentalRate.getRentalRate(connection, insurerCountry, supplier, vc);
            estimatedRentalFee = estimatedRentalFee.add(rr.getCost().multiply(rv.getDays()));
        }
        ArrayList<RentalExtra> extras = RentalExtra.getForRental(connection, rental);
        BigDecimal rentalExtraFees = new BigDecimal("0.00");
        for (RentalExtra re : extras) {
            Extra e = Extra.instantiate(connection, re.getExtraID());
            RentalExtraRate rer = RentalExtraRate.getRentalExtraRate(connection, insurerCountry, supplier, e);
            rentalExtraFees = rentalExtraFees.add(rer.getCost());
        }
        rentalExtraFees = rentalExtraFees.multiply(rentalDays);
        
        RentalCost rentalCost=RentalCost.getForRental(connection, rental);

        if (rentalDays.doubleValue() <= engineerReport.getDays().doubleValue() && estimatedRentalFee.doubleValue() < invoice.getNet().doubleValue()) {
            return Boolean.TRUE;
        }
        
        if (rentalDays.doubleValue() <= engineerReport.getDays().doubleValue() && estimatedRentalFee.doubleValue() < rentalCost.getNetRental().doubleValue()) {
            return Boolean.TRUE;
        }
        
        try {
            BigDecimal allowedExtraDays = parameters.get("allowed extra rental days").getNumericValue();
            if (rentalDays.doubleValue() > engineerReport.getDays().add(allowedExtraDays).doubleValue()) {
                return false;
            }
        } catch (Exception e) {
        }

        try {
            BigDecimal allowedNetAmountUplift = new BigDecimal("0.000000");
            allowedNetAmountUplift = allowedNetAmountUplift.add(parameters.get("allowed net amount percentage uplift").getNumericValue()).divide(new BigDecimal("100.000000"), BigDecimal.ROUND_HALF_UP).add(new BigDecimal("1.000000"));
            
            if (invoiceNet.doubleValue() < estimatedRentalFee.add(rentalExtraFees).multiply(allowedNetAmountUplift).doubleValue()) {
                return Boolean.TRUE;
            }
            
            if (rentalCost.getNetRental().doubleValue() < estimatedRentalFee.add(rentalExtraFees).multiply(allowedNetAmountUplift).doubleValue()) {
                return Boolean.TRUE;
            }
                        
        } catch (Exception e) {
        }

        return Boolean.FALSE;
    }
}
