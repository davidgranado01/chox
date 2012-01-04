package idas.chox.core.services;

import idas.chox.core.model.ClaimType;
import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author John
 */
public interface VehicleClassPriceService {
   public BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, int insId, int choId) throws Exception;
   public BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, BigDecimal age ,  int insId, int choId) throws Exception;
}
