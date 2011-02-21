package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author John
 */
public interface VehicleClassPriceService {
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId);
  // public List<VehicleClassPrice> getPrices(VehicleClass vehicleClass, Date startDate);
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age ,  int insId, int choId);
}
