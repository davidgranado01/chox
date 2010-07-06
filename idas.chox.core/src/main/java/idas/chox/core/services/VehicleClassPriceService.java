package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author John
 */
public interface VehicleClassPriceService {
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate);
}
