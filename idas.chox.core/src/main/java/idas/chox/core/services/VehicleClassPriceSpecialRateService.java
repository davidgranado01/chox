package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author seeni
 */
public interface VehicleClassPriceSpecialRateService {

   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId);
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId);
}

